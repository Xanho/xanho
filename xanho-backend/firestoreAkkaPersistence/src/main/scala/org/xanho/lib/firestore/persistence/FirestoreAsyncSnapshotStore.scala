package org.xanho.lib.firestore.persistence

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.persistence.snapshot.SnapshotStore
import akka.persistence.{SelectedSnapshot, SnapshotMetadata, SnapshotSelectionCriteria}
import akka.serialization._
import akka.stream.scaladsl.Sink
import com.google.cloud.firestore.{Blob, CollectionReference}
import com.google.cloud.firestore.Query.Direction
import com.typesafe.config.Config
import org.xanho.lib.firestore.FirestoreApi
import org.xanho.lib.firestore.implicits.FirestoreFutureHelper

import scala.collection.JavaConverters._
import scala.concurrent.Future

class FirestoreAsyncSnapshotStore(config: Config) extends SnapshotStore {

  private implicit val system: ActorSystem[_] =
    context.system.toTyped

  private val firestoreApi = FirestoreApi(system)

  private val serialization = SerializationExtension(system)

  import context.dispatcher
  import firestoreApi._

  private val snapshotStoreCollection: String =
    config.withFallback(
      system.settings.config.getConfig("akka.persistence.snapshot-store.firestore")
    )
      .getString("firestore-collection")

  override def loadAsync(persistenceId: String, criteria: SnapshotSelectionCriteria): Future[Option[SelectedSnapshot]] =
    selectSnapshots(persistenceId, criteria)
      .orderBy("metadata.sequenceNr", Direction.DESCENDING)
      .unfoldSource(1)
      .map(_.getData.asScala.toMap)
      .map(mapToSelectedSnapshot)
      .filter(snapshot =>
        snapshot.metadata.timestamp >= criteria.minTimestamp &&
          snapshot.metadata.timestamp <= criteria.maxTimestamp
      )
      .runWith(Sink.headOption)

  override def saveAsync(metadata: SnapshotMetadata, snapshot: Any): Future[Unit] =
    createDocumentIfNotExists(
      firestore.collection(snapshotStoreCollection)
        .document(metadata.persistenceId)
    ).flatMap(_ =>
      snapshotCollectionReference(metadata.persistenceId)
        .document(metadata.sequenceNr.toString)
        .set(snapshotToMap(metadata, snapshot).asJava)
        .scalaFuture
        .map(_ => ())
    )

  override def deleteAsync(metadata: SnapshotMetadata): Future[Unit] =
    snapshotCollectionReference(metadata.persistenceId)
      .document(metadata.sequenceNr.toString)
      .delete()
      .scalaFuture
      .map(_ => ())

  override def deleteAsync(persistenceId: String, criteria: SnapshotSelectionCriteria): Future[Unit] =
    selectSnapshots(persistenceId, criteria)
      .orderBy("metadata.sequenceNr", Direction.DESCENDING)
      .select("metadata.sequenceNr", "metadata.timestamp")
      .unfoldSource(10)
      .filter { snapshot =>
        val timestamp = Option(snapshot.get("metadata.timestamp")).fold(0L)(_.asInstanceOf[Long])
        timestamp >= criteria.minTimestamp &&
          timestamp <= criteria.maxTimestamp
      }
      .map(_.getReference)
      .grouped(10)
      .map(group =>
        group.foldLeft(firestore.batch())(
          (batch, snapshot) =>
            batch.delete(snapshot)
        ).commit().scalaFuture
      )
      .mapAsync(1)(identity)
      .runWith(Sink.ignore)
      .map(_ => ())

  private def snapshotCollectionReference(persistenceId: String): CollectionReference =
    firestore.collection(snapshotStoreCollection)
      .document(persistenceId)
      .collection("snapshots")

  // Note: Firestore does not support comparison filters on more than one property, so timestamp is not checked
  private def selectSnapshots(persistenceId: String, criteria: SnapshotSelectionCriteria) =
    snapshotCollectionReference(persistenceId)
      .whereGreaterThanOrEqualTo("metadata.sequenceNr", criteria.minSequenceNr)
      .whereLessThanOrEqualTo("metadata.sequenceNr", criteria.maxSequenceNr)

  def snapshotToMap(metadata: SnapshotMetadata, payload: Any): Map[String, AnyRef] = {
    val boxedPayload =
      box(payload)
    val serializer =
      serialization.findSerializerFor(boxedPayload)

    Map(
      "payload" -> Blob.fromBytes(serialization.serialize(boxedPayload).get),
      "payloadSerializerId" -> Int.box(serializer.identifier),
      "payloadManifest" -> Serializers.manifestFor(serializer, boxedPayload),
      "metadata" -> snapshotMetadataToMap(metadata).asJava
    )
  }

  def mapToSelectedSnapshot(map: Map[String, AnyRef]): SelectedSnapshot = {
    val payload =
      serialization.deserialize(
        map("payload").asInstanceOf[Blob].toBytes,
        Int.box(map("payloadSerializerId").asInstanceOf[Number].intValue()),
        map("payloadManifest").asInstanceOf[String]
      ).get
    SelectedSnapshot(
      metadata = mapToSnapshotMetadata(
        map("metadata").asInstanceOf[java.util.Map[String, AnyRef]].asScala.toMap
      ),
      snapshot = payload
    )
  }

  def mapToSnapshotMetadata(map: Map[String, AnyRef]): SnapshotMetadata =
    SnapshotMetadata(
      persistenceId = map.getOrElse("persistenceId", "0").toString,
      sequenceNr = Long.box(map.getOrElse("sequenceNr", 0L).asInstanceOf[Number].longValue()),
      timestamp = Long.box(map.getOrElse("timestamp", 0L).asInstanceOf[Number].longValue())
    )

  def snapshotMetadataToMap(snapshotMetadata: SnapshotMetadata): Map[String, AnyRef] =
    Map(
      "persistenceId" -> snapshotMetadata.persistenceId,
      "sequenceNr" -> Long.box(snapshotMetadata.sequenceNr),
      "timestamp" -> Long.box(snapshotMetadata.timestamp)
    )
}

object FirestoreAsyncSnapshotStore {

}

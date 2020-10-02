package org.xanho.lib.firestore.persistence

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.persistence.snapshot.SnapshotStore
import akka.persistence.{PersistentRepr, SelectedSnapshot, SnapshotMetadata, SnapshotSelectionCriteria}
import com.google.cloud.firestore.CollectionReference
import org.xanho.lib.firestore.FirestoreApi
import org.xanho.lib.firestore.implicits.FirestoreFutureHelper

import scala.collection.JavaConverters._
import scala.concurrent.Future

class FirestoreAsyncSnapshotStore extends SnapshotStore {

  private implicit val system: ActorSystem[_] =
    context.system.toTyped

  private val firestoreApi = FirestoreApi(system)

  import context.dispatcher
  import firestoreApi._

  override def loadAsync(persistenceId: String, criteria: SnapshotSelectionCriteria): Future[Option[SelectedSnapshot]] =
    selectSnapshots(persistenceId, criteria)
      .orderBy("metadata.sequenceNr")
      .limitToLast(1)
      .get()
      .scalaFuture
      .map(snapshot =>
        snapshot.getDocuments.asScala
          .headOption
          .map(_.toObject(classOf[SelectedSnapshot]))
      )

  override def saveAsync(metadata: SnapshotMetadata, snapshot: Any): Future[Unit] =
    snapshotCollectionReference(metadata.persistenceId)
      .document(metadata.sequenceNr.toString)
      .set(
        Map(
          "metadata" -> metadata,
          "snapshot" -> snapshot
        ).asJava
      )
      .scalaFuture
      .map(_ => ())

  override def deleteAsync(metadata: SnapshotMetadata): Future[Unit] =
    snapshotCollectionReference(metadata.persistenceId)
      .document(metadata.sequenceNr.toString)
      .delete()
      .scalaFuture
      .map(_ => ())

  override def deleteAsync(persistenceId: String, criteria: SnapshotSelectionCriteria): Future[Unit] =
    selectSnapshots(persistenceId, criteria)
      .deleteAll()
      .map(_ => ())

  private def snapshotCollectionReference(persistenceId: String): CollectionReference =
    firestore.collection("snapshot-store")
      .document(persistenceId)
      .collection("snapshots")

  private def selectSnapshots(persistenceId: String, criteria: SnapshotSelectionCriteria) =
    snapshotCollectionReference(persistenceId)
      .whereGreaterThanOrEqualTo("metadata.sequenceNr", criteria.minSequenceNr)
      .whereLessThanOrEqualTo("metadata.sequenceNr", criteria.maxSequenceNr)
      .whereGreaterThanOrEqualTo("metadata.timestamp", criteria.minTimestamp)
      .whereLessThanOrEqualTo("metadata.timestamp", criteria.maxTimestamp)
}

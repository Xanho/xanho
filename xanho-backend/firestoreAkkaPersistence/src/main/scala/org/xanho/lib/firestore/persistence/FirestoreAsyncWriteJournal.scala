package org.xanho.lib.firestore.persistence

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.persistence.journal.AsyncWriteJournal
import akka.persistence.{AtomicWrite, PersistentRepr}
import akka.serialization._
import akka.stream.scaladsl.{Sink, Source}
import com.google.cloud.firestore.{Blob, CollectionReference, DocumentReference, SetOptions}
import com.typesafe.config.Config
import org.xanho.lib.firestore.FirestoreApi
import org.xanho.lib.firestore.implicits.FirestoreFutureHelper

import scala.collection.JavaConverters._
import scala.collection.immutable
import scala.concurrent.Future
import scala.util.{Success, Try}

class FirestoreAsyncWriteJournal(config: Config) extends AsyncWriteJournal {

  private implicit val system: ActorSystem[_] =
    context.system.toTyped

  private val firestoreApi = FirestoreApi(system)

  private val serialization = SerializationExtension(system)

  import context.dispatcher
  import firestoreApi._

  private val writeJournalCollection: String =
    config.withFallback(
      system.settings.config.getConfig("default-akka-persistence-write-journal-settings")
    )
      .getString("firestore-collection")

  override def asyncWriteMessages(messages: immutable.Seq[AtomicWrite]): Future[immutable.Seq[Try[Unit]]] =
    Source(messages)
      .map(atomicWrite =>
        createDocumentIfNotExists(
          persistenceReference(atomicWrite.persistenceId)
        )
          .flatMap(_ =>
            atomicWrite.payload
              .foldLeft(firestore.batch())(
                (batchWrite, repr) =>
                  batchWrite.set(
                    eventsReference(repr.persistenceId).document(repr.sequenceNr.toString),
                    persistenceReprToMap(repr).asJava
                  )
              )
              .commit().scalaFuture
              .map(_ => ())
              .transform(Success(_))
          )
      )
      .mapAsync(10)(identity)
      .runWith(Sink.seq)

  override def asyncDeleteMessagesTo(persistenceId: String, toSequenceNr: Long): Future[Unit] =
    eventsReference(persistenceId)
      .whereLessThanOrEqualTo("sequenceNr", toSequenceNr)
      .orderBy("sequenceNr")
      .select("sequenceNr")
      .unfoldSource
      .map(snapshot =>
        snapshot.getReference.set(Map("deleted" -> true).asJava, SetOptions.merge()).scalaFuture
      )
      .mapAsync(10)(identity)
      .runWith(Sink.ignore)
      .map(_ => ())

  override def asyncReplayMessages(persistenceId: String, fromSequenceNr: Long, toSequenceNr: Long, max: Long)(recoveryCallback: PersistentRepr => Unit): Future[Unit] =
    eventsReference(persistenceId)
      .whereGreaterThanOrEqualTo("sequenceNr", fromSequenceNr)
      .whereLessThanOrEqualTo("sequenceNr", toSequenceNr)
      .orderBy("sequenceNr")
      .unfoldSource
      .map(_.getData.asScala.toMap)
      .map(mapToRepr)
      .filterNot(_.deleted)
      .take(max)
      .runForeach(recoveryCallback)
      .map(_ => ())

  override def asyncReadHighestSequenceNr(persistenceId: String, fromSequenceNr: Long): Future[Long] =
    eventsReference(persistenceId)
      .whereGreaterThanOrEqualTo("sequenceNr", fromSequenceNr)
      .orderBy("sequenceNr")
      .select("sequenceNr")
      .limitToLast(1)
      .get()
      .scalaFuture
      .map(
        _.getDocuments.asScala
          .lastOption
          .fold(0L)(_.get("sequenceNr", classOf[Long]))
          .max(fromSequenceNr)
      )

  private def persistenceReference(persistenceId: String): DocumentReference =
    firestore.collection(writeJournalCollection)
      .document(persistenceId)

  private def eventsReference(persistenceId: String): CollectionReference =
    persistenceReference(persistenceId)
      .collection("events")

  def mapToRepr(map: Map[String, AnyRef]): PersistentRepr = {
    val payload =
      serialization.deserialize(
        map("payload").asInstanceOf[Blob].toBytes,
        map("payloadSerializerId").asInstanceOf[Number].intValue(),
        map("payloadManifest").asInstanceOf[String]
      ).get
    PersistentRepr(
      payload = payload,
      sequenceNr = map("sequenceNr").asInstanceOf[java.lang.Number].longValue(),
      persistenceId = map("persistenceId").asInstanceOf[String],
      manifest = map.getOrElse("manifest", PersistentRepr.Undefined).asInstanceOf[String],
      deleted = map.get("deleted").contains(true),
      sender = null,
      writerUuid = map.getOrElse("writerUuid", PersistentRepr.Undefined).asInstanceOf[String]
    )
  }

  def persistenceReprToMap(repr: PersistentRepr): Map[String, AnyRef] = {
    val boxedPayload =
      box(repr.payload)
    val serializer =
      serialization.findSerializerFor(boxedPayload)
    Map(
      "payload" -> Blob.fromBytes(serialization.serialize(boxedPayload).get),
      "payloadSerializerId" -> Int.box(serializer.identifier),
      "payloadManifest" -> Serializers.manifestFor(serializer, boxedPayload),
      "persistenceId" -> repr.persistenceId,
      "sequenceNr" -> Long.box(repr.sequenceNr),
      "timestamp" -> Long.box(repr.timestamp),
      "manifest" -> repr.manifest,
      "deleted" -> Boolean.box(repr.deleted),
      "writerUuid" -> repr.writerUuid,
    )
  }
}

object FirestoreAsyncWriteJournal {

}
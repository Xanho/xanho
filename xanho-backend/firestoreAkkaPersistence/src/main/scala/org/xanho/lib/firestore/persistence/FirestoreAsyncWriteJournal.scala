package org.xanho.lib.firestore.persistence

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.persistence.journal.AsyncWriteJournal
import akka.persistence.{AtomicWrite, PersistentRepr}
import akka.stream.scaladsl.{Sink, Source}
import com.google.cloud.firestore.{CollectionReference, DocumentReference, SetOptions}
import org.xanho.lib.firestore.FirestoreApi
import org.xanho.lib.firestore.implicits.FirestoreFutureHelper

import scala.collection.JavaConverters._
import scala.collection.immutable
import scala.concurrent.Future
import scala.util.{Success, Try}

class FirestoreAsyncWriteJournal extends AsyncWriteJournal {

  private implicit val system: ActorSystem[_] =
    context.system.toTyped

  private val firestoreApi = FirestoreApi(system)

  import context.dispatcher
  import firestoreApi._

  override def asyncWriteMessages(messages: immutable.Seq[AtomicWrite]): Future[immutable.Seq[Try[Unit]]] =
    Source(messages)
      .map(atomicWrite =>
        atomicWrite.payload
          .foldLeft(firestore.batch())(
            (batchWrite, repr) =>
              batchWrite.set(
                eventsReference(repr.persistenceId).document(repr.sequenceNr.toString),
                repr.asInstanceOf[Product].toJavaMap
              )
          )
          .commit().scalaFuture
          .map(_ => ())
          .transform(Success(_))
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
      .map(FirestoreAsyncWriteJournal.mapToRepr)
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
    firestore.collection("persistence-events")
      .document(persistenceId)

  private def eventsReference(persistenceId: String): CollectionReference =
    persistenceReference(persistenceId)
      .collection("events")
}

object FirestoreAsyncWriteJournal {
  def mapToRepr(map: Map[String, AnyRef]): PersistentRepr =
    PersistentRepr(
      payload = map("payload"),
      sequenceNr = map("sequenceNr").asInstanceOf[java.lang.Number].longValue(),
      persistenceId = map("persistenceId").asInstanceOf[String],
      manifest = map.getOrElse("manifest", PersistentRepr.Undefined).asInstanceOf[String],
      deleted = map.get("deleted").contains(true),
      sender = null,
      writerUuid = map.getOrElse("writerUuid", PersistentRepr.Undefined).asInstanceOf[String]
    )
}
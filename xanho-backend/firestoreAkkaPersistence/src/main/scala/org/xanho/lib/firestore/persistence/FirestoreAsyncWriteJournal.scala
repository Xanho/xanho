package org.xanho.lib.firestore.persistence

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.persistence.journal.AsyncWriteJournal
import akka.persistence.{AtomicWrite, PersistentRepr}
import akka.serialization._
import akka.stream.scaladsl.{Sink, Source}
import com.google.cloud.firestore.{CollectionReference, DocumentReference, SetOptions}
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
      system.settings.config.getConfig("akka.persistence.journal.firestore")
    )
      .getString("firestore-collection")

  override def asyncWriteMessages(messages: immutable.Seq[AtomicWrite]): Future[immutable.Seq[Try[Unit]]] =
    Source(messages)
      .mapAsync(10)(handleAtomicWrite)
      .runWith(Sink.seq)

  private def handleAtomicWrite(atomicWrite: AtomicWrite): Future[Try[Unit]] =
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
}

object FirestoreAsyncWriteJournal {

}
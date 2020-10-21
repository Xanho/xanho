package org.xanho.lib.firestore.persistence

import akka.NotUsed
import akka.actor.ExtendedActorSystem
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter.ClassicActorSystemOps
import akka.persistence.PersistentRepr
import akka.persistence.query.scaladsl._
import akka.persistence.query.{EventEnvelope, Offset, ReadJournalProvider}
import akka.stream.scaladsl.{BroadcastHub, Source}
import com.google.cloud.firestore.DocumentChange
import com.typesafe.config.Config
import org.xanho.lib.firestore.FirestoreApi
import org.xanho.lib.firestore.persistence.javadsl.{FirestoreReadJournal => JFirestoreReadJournal}
import scala.collection.JavaConverters._

class FirestoreReadJournal(extendedActorSystem: ExtendedActorSystem, config: Config)
  extends ReadJournal
    with PersistenceIdsQuery
    with CurrentPersistenceIdsQuery
    with EventsByPersistenceIdQuery
    with CurrentEventsByPersistenceIdQuery {

  private implicit val system: ActorSystem[_] =
    extendedActorSystem.toTyped

  private val firestoreApi = FirestoreApi(system)

  import firestoreApi._

  private val firestoreCollection =
    config.getString("firestore-collection")

  private val reusableNewPersistenceIdSource: Source[String, NotUsed] =
    firestore.collection(firestoreCollection)
      .select(Array.empty[String]: _*)
      .querySnapshotStream
      .mapConcat(_.getDocumentChanges.asScala.toList)
      .filter(_.getType == DocumentChange.Type.ADDED)
      .map(_.getDocument.getId)
      .runWith(BroadcastHub.sink)

  override def persistenceIds(): Source[String, NotUsed] =
    currentPersistenceIds()
      .concat(reusableNewPersistenceIdSource)

  override def currentPersistenceIds(): Source[String, NotUsed] =
    firestore.collection(firestoreCollection)
      .select(Array.empty[String]: _*)
      .unfoldSource
      .map(_.getId)

  override def eventsByPersistenceId(persistenceId: String, fromSequenceNr: Long, toSequenceNr: Long): Source[EventEnvelope, NotUsed] =
    currentEventsByPersistenceId(persistenceId, fromSequenceNr, toSequenceNr)
      .concat(
        firestore.collection(firestoreCollection)
          .document(persistenceId)
          .collection("events")
          .querySnapshotStream
          .mapConcat(_.getDocumentChanges.asScala.toList)
          .filter(_.getType == DocumentChange.Type.ADDED)
          .map(_.getDocument.getData.asScala.toMap)
          .map(mapToRepr)
          .map(persistentReprToEventEnvelope)
      )


  override def currentEventsByPersistenceId(persistenceId: String, fromSequenceNr: Long, toSequenceNr: Long): Source[EventEnvelope, NotUsed] =
    firestore.collection(firestoreCollection)
      .document(persistenceId)
      .collection("events")
      .whereGreaterThanOrEqualTo("sequenceNr", fromSequenceNr)
      .whereLessThanOrEqualTo("sequenceNr", toSequenceNr)
      .orderBy("sequenceNr")
      .unfoldSource
      .map(_.getData.asScala.toMap)
      .map(mapToRepr)
      .filterNot(_.deleted)
      .map(persistentReprToEventEnvelope)

  private def persistentReprToEventEnvelope(repr: PersistentRepr): EventEnvelope =
    EventEnvelope(
      offset = Offset.noOffset,
      persistenceId = repr.persistenceId,
      sequenceNr = repr.sequenceNr,
      event = repr.payload,
      timestamp = repr.timestamp,
      meta = repr.metadata
    )
}

class FirestoreReadJournalProvider(system: ExtendedActorSystem, config: Config) extends ReadJournalProvider {
  override def scaladslReadJournal(): FirestoreReadJournal =
    new FirestoreReadJournal(system, config)

  override def javadslReadJournal(): JFirestoreReadJournal =
    new JFirestoreReadJournal(scaladslReadJournal())
}
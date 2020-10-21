package org.xanho.lib.firestore.persistence.javadsl

import akka.NotUsed
import akka.persistence.query.EventEnvelope
import akka.persistence.query.javadsl._
import akka.stream.javadsl.Source

class FirestoreReadJournal(scalaFirestoreReadJournal: org.xanho.lib.firestore.persistence.FirestoreReadJournal) extends ReadJournal
  with PersistenceIdsQuery
  with CurrentPersistenceIdsQuery
  with EventsByPersistenceIdQuery
  with CurrentEventsByPersistenceIdQuery {
  override def persistenceIds(): Source[String, NotUsed] =
    scalaFirestoreReadJournal.persistenceIds().asJava

  override def currentPersistenceIds(): Source[String, NotUsed] =
    scalaFirestoreReadJournal.currentPersistenceIds().asJava

  override def eventsByPersistenceId(persistenceId: String, fromSequenceNr: Long, toSequenceNr: Long): Source[EventEnvelope, NotUsed] =
    scalaFirestoreReadJournal.eventsByPersistenceId(persistenceId, fromSequenceNr, toSequenceNr).asJava

  override def currentEventsByPersistenceId(persistenceId: String, fromSequenceNr: Long, toSequenceNr: Long): Source[EventEnvelope, NotUsed] =
    scalaFirestoreReadJournal.currentEventsByPersistenceId(persistenceId, fromSequenceNr, toSequenceNr).asJava
}

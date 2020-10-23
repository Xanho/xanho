package org.xanho.lib.firestore.persistence

import akka.actor.ExtendedActorSystem
import akka.persistence.query.ReadJournalProvider
import com.typesafe.config.Config
import org.xanho.lib.firestore.persistence.javadsl.{FirestoreReadJournal => JFirestoreReadJournal}
import org.xanho.lib.firestore.persistence.scaladsl.FirestoreReadJournal

class FirestoreReadJournalProvider(system: ExtendedActorSystem, config: Config) extends ReadJournalProvider {
  override def scaladslReadJournal(): FirestoreReadJournal =
    new FirestoreReadJournal(system, config)

  override def javadslReadJournal(): JFirestoreReadJournal =
    new JFirestoreReadJournal(scaladslReadJournal())
}

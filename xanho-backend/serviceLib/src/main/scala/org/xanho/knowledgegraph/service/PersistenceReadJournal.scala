package org.xanho.knowledgegraph.service

import akka.actor.typed.{ActorSystem, Extension, ExtensionId}
import akka.persistence.query.PersistenceQuery
import akka.persistence.query.scaladsl.{CurrentEventsByPersistenceIdQuery, EventsByPersistenceIdQuery, ReadJournal}

class PersistenceReadJournal(implicit system: ActorSystem[_]) extends Extension {

  private lazy val readJournal =
    PersistenceQuery(system)
      .readJournalFor[ReadJournal with EventsByPersistenceIdQuery with CurrentEventsByPersistenceIdQuery](
        system.settings.config.getString("akka.persistence.query.plugin")
      )

  def apply(): ReadJournal with EventsByPersistenceIdQuery with CurrentEventsByPersistenceIdQuery =
    readJournal

}

object PersistenceReadJournal extends ExtensionId[PersistenceReadJournal] {
  override def createExtension(system: ActorSystem[_]): PersistenceReadJournal =
    new PersistenceReadJournal()(system)
}

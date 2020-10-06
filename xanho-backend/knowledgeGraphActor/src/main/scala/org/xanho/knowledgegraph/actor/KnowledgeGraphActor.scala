package org.xanho.knowledgegraph.actor

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.adapter._
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior, RetentionCriteria}
import org.xanho.nlp.ops.NlpStringOps.nlpStringOps
import org.xanho.proto
import org.xanho.proto.knowledgegraphactor._

object KnowledgeGraphActor {

  def apply(id: String): Behavior[KnowledgeGraphCommand] =
    EventSourcedBehavior[KnowledgeGraphCommand, KnowledgeGraphEvent, KnowledgeGraphState](
      persistenceId = PersistenceId.ofUniqueId(id),
      emptyState = KnowledgeGraphState(),
      commandHandler = { (state, command) =>
        command match {
          case IngestText(replyTo, text, _) =>
            Effect.persist[KnowledgeGraphEvent, KnowledgeGraphState](TextIngested(text))
              .thenReply[IngestTextResponse](replyTo)(_ => IngestTextResponse())
          case GetState(replyTo, _) =>
            Effect.reply(replyTo)(GetStateResponse().withState(state))
        }
      },
      eventHandler = { (state, event) =>
        event match {
          case TextIngested(text, _) =>
            state.addParseResults(parseToResult(text))
        }
      }
    )
      .withRetention(RetentionCriteria.snapshotEvery(1, 5))

  def parseToResult(text: String): proto.nlp.ParseResult =
    text.parse

}

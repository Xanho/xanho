package org.xanho.knowledgegraph.actor

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.adapter._
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior}
import org.xanho.nlp.ops.implicits._
import org.xanho.proto.knowledgegraphactor._

object KnowledgeGraphActor {

  def apply(id: String): Behavior[KnowledgeGraphCommand] =
    Behaviors.setup(context =>
      EventSourcedBehavior[KnowledgeGraphCommand, KnowledgeGraphEvent, KnowledgeGraphState](
        persistenceId = PersistenceId.ofUniqueId(id),
        emptyState = KnowledgeGraphState(),
        commandHandler = { (state, command) =>
          command match {
            case IngestText(replyTo, text, _) =>
              context.log.debug("Persisting IngestText event.  graphId={}", id)
              Effect.persist[KnowledgeGraphEvent, KnowledgeGraphState](TextIngested(text))
                .thenReply[IngestTextResponse](replyTo)(_ => IngestTextResponse())
            case GetState(replyTo, _) =>
              Effect.reply(replyTo)(GetStateResponse().withState(state))
            case GenerateResponse(replyTo, _) =>
              context.log.debug("Generating response.  graphId={}", id)
              Effect.reply(replyTo)(GenerateResponseResponse(Some(ResponseGenerator.generate(state))))
          }
        },
        eventHandler = { (state, event) =>
          event match {
            case TextIngested(text, _) =>
              state.addParseResults(text.parse)
          }
        }
      )
    )

}

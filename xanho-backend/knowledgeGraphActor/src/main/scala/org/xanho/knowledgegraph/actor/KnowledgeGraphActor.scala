package org.xanho.knowledgegraph.actor

import java.util.UUID

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.adapter._
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior}
import org.xanho.proto.knowledgegraphactor._

object KnowledgeGraphActor {

  def apply(id: String): Behavior[KnowledgeGraphCommand] =
    Behaviors.setup(context =>
      EventSourcedBehavior[KnowledgeGraphCommand, KnowledgeGraphEvent, KnowledgeGraphState](
        persistenceId = PersistenceId.ofUniqueId(id),
        emptyState = KnowledgeGraphState(),
        commandHandler = { (state, command) =>
          command match {
            case IngestTextMessage(replyTo, message, _) =>
              context.log.debug("Persisting IngestText event.  graphId={}", id)
              Effect.persist[KnowledgeGraphEvent, KnowledgeGraphState](TextMessageIngested(message))
                .thenReply[IngestTextResponse](replyTo)(_ => IngestTextResponse())
            case GetState(replyTo, _) =>
              Effect.reply(replyTo)(GetStateResponse().withState(state))
            case GenerateMessage(replyTo, _) =>
              context.log.debug("Generating response.  graphId={}", id)
              val responseMessage =
                TextMessage(
                  id = UUID.randomUUID().toString,
                  source = MessageSource.SYSTEM,
                  timestampMs = System.currentTimeMillis(),
                  text = ResponseGenerator.generate(state)
                )
              Effect.persist(TextMessageIngested(Some(responseMessage)))
                .thenReply(replyTo)(_ => GenerateMessageResponse(Some(responseMessage)))
          }
        },
        eventHandler = { (state, event) =>
          event match {
            case TextMessageIngested(Some(message), _) =>
              state.addMessages(message)
            case _ =>
              state
          }
        }
      )
    )

}

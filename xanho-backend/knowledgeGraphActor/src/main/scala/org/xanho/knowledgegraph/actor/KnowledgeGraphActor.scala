package org.xanho.knowledgegraph.actor

import java.util.UUID

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.adapter._
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior, ReplyEffect}
import org.xanho.nlp.graph.ops.implicits.nlpGraphBuilderOps
import org.xanho.nlp.ops.implicits.nlpStringOps
import org.xanho.proto.graph
import org.xanho.proto.knowledgegraphactor._

object KnowledgeGraphActor {

  def apply(id: String): Behavior[KnowledgeGraphCommand] =
    Behaviors.setup(context =>
      EventSourcedBehavior[KnowledgeGraphCommand, KnowledgeGraphEvent, KnowledgeGraphState](
        persistenceId = PersistenceId.ofUniqueId(id),
        emptyState = KnowledgeGraphState(),
        commandHandler = commandHandler(id, context),
        eventHandler = eventHandler(id, context)
      )
    )

  def commandHandler(id: String, context: ActorContext[KnowledgeGraphCommand])
                    (state: KnowledgeGraphState, command: KnowledgeGraphCommand): ReplyEffect[KnowledgeGraphEvent, KnowledgeGraphState] =
    command match {
      case IngestTextMessage(replyTo, message, _) =>
        context.log.debug("Persisting IngestText event.  graphId={}", id)
        Effect.persist[KnowledgeGraphEvent, KnowledgeGraphState](TextMessageIngested(message))
          .thenRun(_ => context.self.tell(GenerateMessage(context.system.ignoreRef.toClassic)))
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
            text = ResponseGenerator(context.system).generate(state)
          )
        Effect.persist(TextMessageIngested(Some(responseMessage)))
          .thenReply(replyTo)(_ => GenerateMessageResponse(Some(responseMessage)))
    }

  def eventHandler(id: String, context: ActorContext[KnowledgeGraphCommand])
                  (state: KnowledgeGraphState, event: KnowledgeGraphEvent): KnowledgeGraphState =
    event match {
      case TextMessageIngested(Some(message), _) =>
        val updatedGraph =
          Some(message.source)
            .filter(_ == MessageSource.USER)
            .flatMap(_ =>
              message.text.parse.document
                .map(document =>
                  state.graph.getOrElse(graph.Graph.defaultInstance)
                    .withDocument(document)._1
                )
            )
            .orElse(state.graph)
        state.addMessages(message)
          .copy(graph = updatedGraph)
      case _ =>
        state
    }
}

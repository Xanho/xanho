package org.xanho.knowledgegraph.actor

import akka.Done
import akka.actor.typed.Behavior
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior}
import org.xanho.proto.knowledgegraphactor.{GetState, IngestText, KnowledgeGraphCommand, KnowledgeGraphEvent, KnowledgeGraphState, TextIngested}
import akka.actor.typed.scaladsl.adapter._
import org.xanho.nlp.NLP
import org.xanho.proto
import org.xanho.proto.nlp.{Phrase, Text}

object KnowledgeGraphActor {

  def apply(id: String): Behavior[KnowledgeGraphCommand] =
    EventSourcedBehavior[KnowledgeGraphCommand, KnowledgeGraphEvent, KnowledgeGraphState](
      persistenceId = PersistenceId.ofUniqueId(id),
      emptyState = KnowledgeGraphState(),
      commandHandler = { (state, command) =>
        command match {
          case IngestText(replyTo, text, _) =>
            Effect.persist[KnowledgeGraphEvent, KnowledgeGraphState](TextIngested(text))
              .thenReply[Done](replyTo)(_ => Done)
          case GetState(replyTo, _) =>
            Effect.reply(replyTo)(state)
        }
      },
      eventHandler = { (state, event) =>
        event match {
          case TextIngested(text, _) =>
            state.copy(parseResults = state.parseResults :+ parseToResult(text))
        }
      }
    )

  def parseToResult(text: String): proto.nlp.ParseResult =
    proto.nlp.ParseResult(
      text = Some(Text(text)),
      sentences =
        NLP.parse(text)
          .map(s =>
            proto.nlp.Sentence(
              Some(Phrase(s.phrase.map(w => proto.nlp.Token.Word(w.value)))),
              s.punctuation.map(p => proto.nlp.Token.Punctuation(p.value))
            )
          )
    )

}

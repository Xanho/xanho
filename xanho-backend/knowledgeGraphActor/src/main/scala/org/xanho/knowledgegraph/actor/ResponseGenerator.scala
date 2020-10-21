package org.xanho.knowledgegraph.actor

import org.xanho.knowledgegraph.actor.implicits.KnowledgeGraphImplicits
import org.xanho.proto.knowledgegraphactor.KnowledgeGraphState

import scala.util.Random

object ResponseGenerator {

  def generate(state: KnowledgeGraphState): String =
    if (state.messages.isEmpty)
      "Tell me a cool fact."
    else {
      val topic = pickTopic(state)
      s"Tell me about '$topic'."
    }

  def pickTopic(state: KnowledgeGraphState): String = {
    val vocabulary = state.vocabulary.map(_.value).toIndexedSeq
    vocabulary(Random.nextInt(vocabulary.size))
  }

}

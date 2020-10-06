package org.xanho.knowledgegraph.actor

import org.xanho.knowledgegraph.actor.implicits.KnowledgeGraphImplicits
import org.xanho.proto.knowledgegraphactor.KnowledgeGraphState
import org.xanho.proto.nlp
import org.xanho.nlp.ops.implicits._

import scala.util.Random

object ResponseGenerator {

  def generate(state: KnowledgeGraphState): nlp.Document =
    if(state.parseResults.isEmpty)
      "Tell me a cool fact.".tokens.asDocument
    else {
      val topic = pickTopic(state)
      s"Tell me about '$topic'.".tokens.asDocument
    }

  def pickTopic(state: KnowledgeGraphState): String = {
    val vocabulary = state.vocabulary.map(_.value).toIndexedSeq
    vocabulary(Random.nextInt(vocabulary.size))
  }

}

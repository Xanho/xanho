package org.xanho.knowledgegraph.actor

import akka.actor.typed.{ActorSystem, Extension, ExtensionId}
import org.xanho.knowledgegraph.actor.implicits.KnowledgeGraphImplicits
import org.xanho.nlp.opennlp.OpenNlpApi
import org.xanho.proto.knowledgegraphactor.KnowledgeGraphState

class ResponseGenerator extends Extension {

  private val openNlpApi = new OpenNlpApi

  def generate(state: KnowledgeGraphState): String =
    if (state.messages.isEmpty)
      "Tell me a cool fact."
    else {
      val topic = pickTopic(state)
      s"Tell me about '$topic'."
    }

  def pickTopic(state: KnowledgeGraphState): String = {

    val sentences = state.userMessages.map(_.text).flatMap(openNlpApi.sentences)

    val tags = sentences.flatMap(openNlpApi.posTag)

    val interestingWords =
      tags.collect { case (word, tag) if interestingPosTags.contains(tag) => word }

    interestingWords match {
      case Nil => "dolphins"
      case words =>
        words.groupBy(identity).minBy(_._2.length)._1
    }
  }

  private val interestingPosTags =
    Set("JJ", "JJR", "JJS", "NN", "NNS", "NNP", "NNPS", "VB", "VBD", "VBG", "VBN", "VBP", "VBZ")

}

object ResponseGenerator extends ExtensionId[ResponseGenerator] {
  override def createExtension(system: ActorSystem[_]): ResponseGenerator = new ResponseGenerator
}

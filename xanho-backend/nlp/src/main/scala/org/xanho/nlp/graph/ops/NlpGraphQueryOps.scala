package org.xanho.nlp.graph.ops

import org.xanho.graph.ops.implicits._
import org.xanho.nlp.Constants
import org.xanho.nlp.graph._
import org.xanho.nlp.graph.ops.implicits._
import org.xanho.proto.graph._
import org.xanho.proto.nlp

import scala.language.implicitConversions

trait NlpGraphQueryOps {

  implicit def nlpGraphReaderOps(graph: Graph): NlpGraphQuery =
    new NlpGraphQuery(graph)

}

object NlpGraphQueryOps extends NlpGraphQueryOps

class NlpGraphQuery(val graph: Graph) extends AnyVal {

  def wordNode(word: nlp.Token.Word): Option[WordNode] =
    graph.nodesByType(NodeTypes.Word)
      .find(node =>
        node.dataOrEmpty.values.get("value").exists(_.getStringValue == word.value)
      )

  def punctuationNode(punctuation: nlp.Token.Punctuation): Option[PunctuationNode] =
    graph.nodesByType(NodeTypes.Punctuation)
      .find(node =>
        node.dataOrEmpty.values("value").getStringValue == punctuation.value
      )

  def wordNodes: Stream[WordNode] =
    graph.nodesByType(NodeTypes.Word)

  def punctuationNodes: Stream[PunctuationNode] =
    graph.nodesByType(NodeTypes.Punctuation)

  def sentenceNodes: Stream[SentenceNode] =
    graph.nodesByType(NodeTypes.Sentence)

  def paragraphNodes: Stream[ParagraphNode] =
    graph.nodesByType(NodeTypes.Paragraph)

  def documentNodes: Stream[DocumentNode] =
    graph.nodesByType(NodeTypes.Document)

  def questions: Stream[SentenceNode] =
    sentenceNodes
      .filter(sentenceNode =>
        sentenceNode.sentencePunctuation(graph)
          .exists(_.punctuationValue == Constants.QuestionMark)
      )

  def statements: Stream[SentenceNode] =
    sentenceNodes
      .filter(sentenceNode =>
        sentenceNode.sentencePunctuation(graph)
          .exists(p =>
            Set(Constants.Period, Constants.ExclamationMark, Constants.QuestionMark)
              .contains(p.punctuationValue)
          )
      )

  def wordsByPosTag(posTag: String): Stream[WordNode] =
    graph
      .edgesByType(EdgeTypes.PhraseWord)
      .filter(_.dataOrEmpty.values.get("posTag").exists(_.getStringValue == posTag))
      .map(_.destination(graph))

}

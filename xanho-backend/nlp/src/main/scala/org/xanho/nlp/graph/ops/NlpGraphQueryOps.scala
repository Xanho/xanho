package org.xanho.nlp.graph.ops

import org.xanho.dictionary.Dictionary
import org.xanho.graph.ops.implicits._
import org.xanho.nlp.graph._
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
      .find(
        Dictionary()(word.value).fold(
          (node: Node) => node.dataOrEmpty.values.get("value").exists(_.getStringValue == word.value)
        )(idx =>
          (node: Node) => node.dataOrEmpty.values.get("index").exists(_.getIntValue == idx)
        )
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

}
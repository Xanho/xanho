package org.xanho.nlp.graph.ops

import org.xanho.dictionary.Dictionary
import org.xanho.graph.ops.implicits._
import org.xanho.nlp.graph._
import org.xanho.proto.graph._

import scala.language.implicitConversions

trait NlpNodeOps {

  implicit def nlpGraphReaderOps(node: Node): NlpNode =
    new NlpNode(node)

}

object NlpNodeOps extends NlpNodeOps

class NlpNode(val node: Node) extends AnyVal {

  def wordValue: String =
    node.dataOrEmpty.values.get("index")
      .map(_.getIntValue)
      .flatMap(Dictionary()(_))
      .getOrElse(node.dataOrEmpty.values("value").getStringValue)

  def punctuationValue: Char =
    node.dataOrEmpty.values("value").getStringValue.head

  def resolvedWordNode: Node =
    node.dataOrEmpty.values.get("index")
      .map(indexData =>
        node.withData(
          DataObject(Map("value" -> Data().withStringValue(Dictionary()(indexData.getIntValue).get)))
        )
      )
      .getOrElse(node)

  def phraseWords(implicit graph: Graph): Stream[WordNode] =
    node.sourceEdges
      .filter(_.edgeType == EdgeTypes.PhraseWord)
      .sortBy(_.dataOrEmpty.values("index").getIntValue)
      .map(_.destination)

  def wordPhrases(implicit graph: Graph): Stream[PhraseNode] =
    node.destinationEdges
      .filter(_.edgeType == EdgeTypes.PhraseWord)
      .map(_.source)

  def phraseSentence(implicit graph: Graph): SentenceNode =
    node.destinationEdges
      .find(_.edgeType == EdgeTypes.SentencePhrase)
      .get.source

  def sentencePhrase(implicit graph: Graph): PhraseNode =
    node.sourceEdges
      .find(_.edgeType == EdgeTypes.SentencePhrase)
      .get.destination

  def punctuationSentences(implicit graph: Graph): Stream[SentenceNode] =
    node.destinationEdges
      .filter(_.edgeType == EdgeTypes.SentencePunctuation)
      .map(_.source)

  def sentencePunctuation(implicit graph: Graph): Option[PunctuationNode] =
    node.sourceEdges
      .find(_.edgeType == EdgeTypes.SentencePunctuation)
      .map(_.destination)

  def paragraphSentences(implicit graph: Graph): Stream[SentenceNode] =
    node.sourceEdges
      .filter(_.edgeType == EdgeTypes.ParagraphSentence)
      .sortBy(_.dataOrEmpty.values("index").getIntValue)
      .map(_.destination)

  def sentenceParagraph(implicit graph: Graph): ParagraphNode =
    node.destinationEdges
      .find(_.edgeType == EdgeTypes.ParagraphSentence)
      .get.source

  def documentParagraphs(implicit graph: Graph): Stream[ParagraphNode] =
    node.sourceEdges
      .filter(_.edgeType == EdgeTypes.DocumentParagraph)
      .sortBy(_.dataOrEmpty.values("index").getIntValue)
      .map(_.destination)

  def paragraphDocument(implicit graph: Graph): DocumentNode =
    node.destinationEdges
      .find(_.edgeType == EdgeTypes.DocumentParagraph)
      .get.source

}

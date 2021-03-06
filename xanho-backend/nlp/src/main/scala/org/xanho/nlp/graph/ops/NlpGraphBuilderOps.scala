package org.xanho.nlp.graph.ops

import org.xanho.graph.ops.implicits._
import org.xanho.nlp.graph._
import org.xanho.nlp.graph.ops.implicits._
import org.xanho.nlp.opennlp.OpenNlpApi
import org.xanho.proto.graph._
import org.xanho.proto.nlp

import scala.annotation.tailrec
import scala.language.implicitConversions

trait NlpGraphBuilderOps {

  implicit def nlpGraphBuilderOps[T](graph: Graph): NlpGraphBuilder[T] =
    new NlpGraphBuilder(graph)

}

object NlpGraphBuilderOps extends NlpGraphBuilderOps

class NlpGraphBuilder[T](val graph: Graph) extends AnyVal {

  def withWordIfNotExists(word: nlp.Token.Word): (Graph, WordNode) =
    graph.wordNode(word).fold(withWord(word))((graph, _))

  def withWord(word: nlp.Token.Word): (Graph, WordNode) = {
    val data =
      Map("value" -> Data().withStringValue(word.value))
    graph.withNode(NodeTypes.Word, DataObject(data))
  }

  def withPunctuationIfNotExists(punctuation: nlp.Token.Punctuation): (Graph, PunctuationNode) =
    graph.punctuationNode(punctuation).fold(withPunctuation(punctuation))((graph, _))

  def withPunctuation(punctuation: nlp.Token.Punctuation): (Graph, PunctuationNode) = {
    val data =
      Map("value" -> Data().withStringValue(punctuation.value))
    graph.withNode(NodeTypes.Punctuation, DataObject(data))
  }

  def withWordWord(wordANode: WordNode, wordBNode: WordNode, distance: Double): (Graph, Edge) = {
    val association =
      1d / distance

    val existingEdgeOpt =
      graph.edges.find(edge => edge.sourceId == wordANode.id && edge.destinationId == wordBNode.id)

    val updatedAssociation =
      existingEdgeOpt
        .flatMap(_.data)
        .flatMap(_.values.get("association"))
        .map(_.getDoubleValue)
        .foldLeft(association)(_ + _)

    val updatedData =
      DataObject(
        existingEdgeOpt
          .flatMap(_.data.map(_.values))
          .getOrElse(Map.empty)
          .updated("association", Data().withDoubleValue(updatedAssociation))
      )

    existingEdgeOpt match {
      case Some(existingEdge) => graph.withEdge(existingEdge.copy(data = Some(updatedData)))
      case _ => graph.withEdge(EdgeTypes.WordWord, wordANode, wordBNode, updatedData)
    }
  }

  def withPhrase(phrase: nlp.Phrase): (Graph, PhraseNode) = {

    val tagged =
      OpenNlpApi().posTag(phrase.words.map(_.value).toList)

    val (graphWithPhrase, phraseNode) =
      graph.withNode(NodeTypes.Phrase, DataObject.defaultInstance)

    val graphWithWords =
      tagged.zipWithIndex
        .foldLeft(graphWithPhrase) {
          case (g, ((word, posTag), index)) =>
            val wordToken =
              nlp.Token.Word(word)
            val (g1, wordNode) =
              g.withWordIfNotExists(wordToken)
            val data =
              Map(
                "index" -> Data().withIntValue(index),
                "posTag" -> Data().withStringValue(posTag)
              )
            val (g2, _) =
              g1.withEdge(EdgeTypes.PhraseWord, phraseNode, wordNode, DataObject(data))
            g2
        }

    (graphWithWords, phraseNode)
  }

  def withSentence(sentence: nlp.Sentence): (Graph, SentenceNode) = {
    val (graphWithPhrase, phraseNode) =
      graph.withPhrase(sentence.phrase.get)

    val (graphWithSentence, sentenceNode) =
      graphWithPhrase.withNode(NodeTypes.Sentence, DataObject.defaultInstance)

    val graphWithPunctuation =
      sentence.punctuation
        .foldLeft(graphWithSentence) {
          case (g, p) =>
            val (g1, punctuationNode) =
              g.withPunctuationIfNotExists(p)
            val (g2, _) =
              g1.withEdge(EdgeTypes.SentencePunctuation, sentenceNode, punctuationNode, DataObject(Map.empty))
            g2
        }

    val (graphWithEdges, _) =
      graphWithPunctuation
        .withEdge(EdgeTypes.SentencePhrase, sentenceNode, phraseNode, DataObject(Map.empty))

    (graphWithEdges, sentenceNode)
  }

  def withParagraph(paragraph: nlp.Paragraph): (Graph, ParagraphNode) = {
    val (graphWithParagraph, paragraphNode) =
      graph.withNode(NodeTypes.Paragraph, DataObject.defaultInstance)

    val withEdges =
      paragraph.sentences
        .zipWithIndex
        .foldLeft(graphWithParagraph) {
          case (g, (sentence, index)) =>
            val (g1, sentenceNode) =
              g.withSentence(sentence)
            val (g2, _) =
              g1.withEdge(EdgeTypes.ParagraphSentence, paragraphNode, sentenceNode, DataObject(Map("index" -> Data().withIntValue(index))))
            g2
        }
    (withEdges, paragraphNode)
  }

  def withDocument(document: nlp.Document): (Graph, DocumentNode) = {
    val (graphWithDocument, documentNode) =
      graph.withNode(NodeTypes.Document, DataObject.defaultInstance)

    val withEdges =
      document.paragraphs
        .zipWithIndex
        .foldLeft(graphWithDocument) {
          case (g, (paragraph, index)) =>
            val (g1, paragraphNode) =
              g.withParagraph(paragraph)
            val (g2, _) =
              g1.withEdge(EdgeTypes.DocumentParagraph, documentNode, paragraphNode, DataObject(Map("index" -> Data().withIntValue(index))))
            g2
        }

    val wordNodes = {
      implicit val g: Graph = withEdges
      documentNode.documentParagraphs
        .flatMap(_.paragraphSentences)
        .map(_.sentencePhrase)
        .flatMap(_.phraseWords)
        .toList
    }

    @tailrec
    def unfoldWordNodes(graph: Graph, wordNodes: List[WordNode]): Graph =
      wordNodes match {
        case Nil =>
          graph
        case head :: tail =>
          unfoldWordNodes(
            tail
              .take(8)
              .zipWithIndex
              .foldLeft(graph) {
                case (g, (destinationWord, distance)) =>
                  g.withWordWord(head, destinationWord, distance + 1)._1
              },
            tail
          )
      }

    val withWordWords =
      unfoldWordNodes(withEdges, wordNodes)

    (withWordWords, documentNode)
  }

}
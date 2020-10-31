package org.xanho.nlp.graph.ops

import org.xanho.graph.ops.implicits._
import org.xanho.nlp.graph._
import org.xanho.nlp.graph.ops.implicits._
import org.xanho.proto.graph._
import org.xanho.proto.nlp

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

  def withPhrase(phrase: nlp.Phrase): (Graph, PhraseNode) = {

    val (graphWithPhrase, phraseNode) =
      graph.withNode(NodeTypes.Phrase, DataObject.defaultInstance)

    val (graphWithWords, _) =
      phrase.words.zipWithIndex
        .foldLeft((graphWithPhrase, None: Option[Node])) {
          case ((g, previousWord), (word, index)) =>
            val (g1, wordNode) =
              g.withWordIfNotExists(word)
            val (g2, _) =
              g1.withEdge(EdgeTypes.PhraseWord, phraseNode, wordNode, DataObject(Map("index" -> Data().withIntValue(index))))
            val g3 =
              previousWord.fold(g2)(previousWord =>
                g2.withEdge(EdgeTypes.WordWord, previousWord, wordNode, DataObject())._1
              )
            (g3, Some(wordNode))
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
    (withEdges, documentNode)
  }

}
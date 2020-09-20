package org.xanho.knowledgegraph.actor.implicits.ops

import org.xanho.knowledgegraph.actor.implicits.ops
import org.xanho.proto.knowledgegraphactor.KnowledgeGraphState
import org.xanho.proto.nlp.{Sentence, Token}
import org.xanho.knowledgegraph.actor.implicits.WordImplicits

class KnowledgeGraphOps(self: KnowledgeGraphState) {

  def allSentences: Seq[Sentence] =
    self.parseResults
      .flatMap(_.sentences)

  def sentencesIncludingWord(word: Token.Word): Seq[Sentence] = {
    val needle = word.toLowerCase
    allSentences
      .filter(_.phrase.exists(_.words.map(_.toLowerCase).contains(needle)))
  }

  def sentencesIncludingAnyWord(words: Set[Token.Word]): Seq[Sentence] = {
    val needles = words.map(_.toLowerCase)
    allSentences
      .filter(_.phrase.exists(_.words.map(_.toLowerCase).exists(needles)))
  }

  def sentencesIncludingAllWords(words: Set[Token.Word]): Seq[Sentence] = {
    val needles = words.map(_.toLowerCase)
    allSentences
      .filter(_.phrase.exists { p =>
        val lowerCasePhraseWords = p.words.map(_.toLowerCase)
        needles.forall(lowerCasePhraseWords.contains)
      })
  }

  def sentencesIncludingOrderedWords(words: Seq[Token.Word]): Seq[Sentence] = {
    val needles = words.map(_.toLowerCase)
    allSentences
      .filter(_.phrase.exists { p =>
        val lowerCasePhraseWords = p.words.map(_.toLowerCase)
        lowerCasePhraseWords.sliding(words.size).contains(needles)
      })
  }

  def vocabulary: Set[Token.Word] =
    allSentences
      .toStream
      .flatMap(_.phrase)
      .flatMap(_.words)
      .map(_.toLowerCase)
      .toSet

  def wordFrequencies: Seq[WordFrequency] = {
    val allWords =
      allSentences
        .flatMap(_.phrase)
        .flatMap(_.words)
        .map(_.toLowerCase)

    allWords
      .groupBy(_.value)
      .toStream
      .map {
        case (key, words) =>
          ops.WordFrequency(words.head, words.size.toDouble / allWords.size.toDouble)
      }
      .sortBy(-_.percent)
      .toList
  }

}

case class WordFrequency(word: Token.Word, percent: Double)

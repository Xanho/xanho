package org.xanho.nlp.opennlp

import opennlp.tools.postag.{POSModel, POSTaggerME}
import opennlp.tools.sentdetect.{SentenceDetectorME, SentenceModel}
import opennlp.tools.tokenize.WhitespaceTokenizer

class OpenNlpApi {

  private val sentenceModel =
    new SentenceModel(getClass.getResourceAsStream("/en-sent.bin"))

  private val sentenceDetector =
    new SentenceDetectorME(sentenceModel)

  private val posModel =
    new POSModel(getClass.getResourceAsStream("/en-pos-maxent.bin"))

  private val posTagger =
    new POSTaggerME(posModel)

  def posTag(sentence: String): List[(String, String)] = {
    val words =
      WhitespaceTokenizer.INSTANCE
        .tokenize(sentence)

    posTag(words.toList)
  }

  def posTag(words: List[String]): List[(String, String)] =
    words.zip(posTagger.tag(words.toArray))

  def sentences(text: String): List[String] =
    sentenceDetector.sentDetect(text).toList

}

object OpenNlpApi {
  private lazy val instance = new OpenNlpApi()

  def apply(): OpenNlpApi = instance
}
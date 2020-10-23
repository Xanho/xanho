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
    val tags =
      posTagger.tag(words)

    words.toList.zip(tags)
  }

  def sentences(text: String): List[String] =
    sentenceDetector.sentDetect(text).toList

}

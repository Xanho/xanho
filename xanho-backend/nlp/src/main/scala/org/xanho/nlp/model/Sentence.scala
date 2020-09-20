package org.xanho.nlp.model

case class Sentence(phrase: List[Word], punctuation: Option[Punctuation]) {

  def sentenceType: Sentence.SentenceType =
    punctuation match {
      case Some(Punctuation.QuestionMark) =>
        Sentence.SentenceTypes.Question
      case _ =>
        Sentence.SentenceTypes.Statement
    }

}

object Sentence {
  sealed trait SentenceType
  object SentenceTypes {
    case object Statement extends SentenceType
    case object Question extends SentenceType
  }
}

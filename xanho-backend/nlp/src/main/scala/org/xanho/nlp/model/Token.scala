package org.xanho.nlp.model

sealed trait Token {
  val value: String
}

case class Word(value: String) extends Token

case object Space extends Token {
  override val value: String = " "

  def unapply(arg: Char): Option[this.type] =
    if(arg == ' ') Some(this) else None
}

sealed trait Punctuation extends Token

object Punctuation {

  def unapply(arg: Char): Option[Punctuation] =
    Period.unapply(arg).orElse(ExclamationMark.unapply(arg)).orElse(QuestionMark.unapply(arg))

  case object Period extends Punctuation {
    val value = "."

    def unapply(arg: Char): Option[this.type] =
      if(arg == '.') Some(this) else None
  }

  case object ExclamationMark extends Punctuation {
    val value = "!"

    def unapply(arg: Char): Option[this.type] =
      if(arg == '!') Some(this) else None
  }

  case object QuestionMark extends Punctuation {
    val value = "?"

    def unapply(arg: Char): Option[this.type] =
      if(arg == '?') Some(this) else None
  }

}

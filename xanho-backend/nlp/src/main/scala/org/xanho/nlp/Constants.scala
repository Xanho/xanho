package org.xanho.nlp

import org.xanho.proto.nlp

object Constants {

  final val LineBreak = '\n'
  final val LineBreakToken = nlp.Token(nlp.Token.Value.LineBreak(nlp.Token.LineBreak()))
  final val Space = ' '
  final val SpaceToken = nlp.Token(nlp.Token.Value.Space(nlp.Token.Space()))
  final val Period = '.'
  final val PeriodToken = nlp.Token(nlp.Token.Value.Punctuation(nlp.Token.Punctuation(new String(Array(Period)))))
  final val ExclamationMark = '!'
  final val ExclamationMarkToken = nlp.Token(nlp.Token.Value.Punctuation(nlp.Token.Punctuation(new String(Array(ExclamationMark)))))
  final val QuestionMark = '?'
  final val QuestionMarkToken = nlp.Token(nlp.Token.Value.Punctuation(nlp.Token.Punctuation(new String(Array(QuestionMark)))))

  val PunctuationCharacters: Set[Char] =
    Set(Period, ExclamationMark, QuestionMark)

  val PunctuationTokens: Set[nlp.Token] =
    Set(PeriodToken, ExclamationMarkToken, QuestionMarkToken)

  val WordCharacters: Set[Char] =
    Set('\'', '-') ++
      ('a' to 'z') ++
      ('A' to 'Z') ++
      ('0' to '9')

  final val ValidCharacters: Set[Char] =
    Set(LineBreak, Space) ++
      PunctuationCharacters ++
      WordCharacters
}

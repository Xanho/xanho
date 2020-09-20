package org.xanho.nlp

import org.xanho.nlp.model.{Punctuation, Sentence, Space, Token, Word}

object NLP {

  def parse(paragraph: String): List[Sentence] =
    sentences(tokens(sanitize(paragraph)))

  protected val ValidCharacters: Set[Char] =
    Set(' ', '.', '!', '?', '"', '\'') ++
      ('a' to 'z') ++
      ('A' to 'Z') ++
      ('0' to '1')

  protected def sanitize(paragraph: String): String =
    paragraph.filter(ValidCharacters)

  protected def tokens(paragraph: String): List[Token] =
    paragraph.foldLeft((Nil: List[Token], new StringBuilder)) {
      case ((tokens, stringBuilder), Punctuation(p)) =>
        if (stringBuilder.nonEmpty)
          (tokens :+ Word(stringBuilder.result()) :+ p, new StringBuilder)
        else
          (tokens :+ p, stringBuilder)
      case ((tokens, stringBuilder), Space(space)) =>
        if (stringBuilder.nonEmpty)
          (tokens :+ Word(stringBuilder.result()) :+ space, new StringBuilder)
        else
          (tokens :+ space, stringBuilder)
      case ((tokens, stringBuilder), char) =>
        (tokens, stringBuilder.append(char))
    } match {
      case (tokens, stringBuilder) if stringBuilder.nonEmpty =>
        tokens :+ Word(stringBuilder.result())
      case (tokens, _) =>
        tokens
    }

  protected def sentences(tokens: List[Token]): List[Sentence] =
    tokens
      .foldLeft((Nil: List[Sentence], Nil: List[Word])) {
        case ((sentences, buffer), punctuation: Punctuation) =>
          (sentences :+ Sentence(buffer, Some(punctuation)), Nil)
        case ((sentences, buffer), word: Word) =>
          (sentences, buffer :+ word)
        case (t, Space) =>
          t
      } match {
      case (sentences, Nil) =>
        sentences
      case (sentences, buffer) =>
        sentences :+ Sentence(buffer, None)
    }

}

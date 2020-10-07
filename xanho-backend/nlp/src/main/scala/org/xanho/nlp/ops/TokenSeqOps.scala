package org.xanho.nlp.ops

import org.xanho.nlp.Constants
import org.xanho.proto.nlp

import scala.language.implicitConversions

trait TokenSeqOps {
  implicit def tokenSeqOps(tokens: Seq[nlp.Token]): TokenSeq =
    new TokenSeq(tokens)
}

object TokenSeqOps extends TokenSeqOps

class TokenSeq(val tokens: Seq[nlp.Token]) extends AnyVal {

  import TokenSeqOps._

  def asDocument: nlp.Document =
    nlp.Document()
      .withParagraphs(
        BufferOrEmitStream[nlp.Token, nlp.Paragraph](
          tokens,
          {
            case (Constants.LineBreakToken, currentBuffer) if currentBuffer.nonEmpty =>
              BufferOrEmitStream.Emit(List(currentBuffer.asParagraph))
            case (Constants.LineBreakToken, _) =>
              BufferOrEmitStream.Emit(Nil)
            case (token, _) =>
              BufferOrEmitStream.Buffer(token)
          },
          tokens => List(tokens.asParagraph)
        )
          .toList
      )

  def asParagraph: nlp.Paragraph =
    nlp.Paragraph()
      .withSentences(
        BufferOrEmitStream[nlp.Token, nlp.Sentence](
          tokens,
          {
            case (t, buffer) if Constants.PunctuationTokens.contains(t) =>
              BufferOrEmitStream.Emit(
                List(
                  (buffer :+ t).asSentence
                )
              )
            case (t, _) =>
              BufferOrEmitStream.Buffer(t)
          },
          buffer => List(buffer.asSentence)
        )
          .toList
      )

  def asSentence: nlp.Sentence =
    tokens.indexWhere(Constants.PunctuationTokens.contains) match {
      case -1 =>
        nlp.Sentence()
          .withPhrase(tokens.asPhrase)
      case idx =>
        nlp.Sentence()
          .withPhrase(tokens.take(idx).asPhrase)
          .copy(
            punctuation =
              tokens(idx).value match {
                case nlp.Token.Value.Punctuation(p) =>
                  Some(p)
                case _ =>
                  None
              }
          )
    }

  def asPhrase: nlp.Phrase =
    nlp.Phrase()
      .withWords(
        tokens.collect {
          case nlp.Token(w: nlp.Token.Value.Word, _) => w.value
        }
      )

}

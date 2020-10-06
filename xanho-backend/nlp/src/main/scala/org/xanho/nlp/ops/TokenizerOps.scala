package org.xanho.nlp.ops

import org.xanho.nlp.Constants
import org.xanho.nlp.ops.IterableOps._
import org.xanho.nlp.ops.NlpCharOps.nlpCharOps
import org.xanho.nlp.ops.NlpStringOps._
import org.xanho.proto.nlp
import org.xanho.proto.nlp.Token

import scala.language.implicitConversions

trait TokenizerOps {
  implicit def toTokenizerOps[T: TokenizerImplicits.TokenGenerator](t: T): Tokenizer[T] =
    new Tokenizer[T](t)
}

object TokenizerOps extends TokenizerOps

class Tokenizer[T: TokenizerImplicits.TokenGenerator](val t: T) {
  def tokens: Seq[nlp.Token] =
    implicitly[TokenizerImplicits.TokenGenerator[T]].toTokens(t)

  def write: String =
    tokens
      .map(_.value)
      .map {
        case nlp.Token.Value.Empty => ""
        case _: nlp.Token.Value.LineBreak => new String(Array(Constants.LineBreak))
        case _: nlp.Token.Value.Space => new String(Array(Constants.Space))
        case nlp.Token.Value.Punctuation(nlp.Token.Punctuation(punctuation, _)) => punctuation
        case nlp.Token.Value.Word(nlp.Token.Word(word, _)) => word
      }
      .mkString
}

object TokenizerImplicits {

  import TokenizerOps._

  sealed abstract class TokenGenerator[T] {
    def toTokens(t: T): Seq[nlp.Token]
  }

  implicit case object StringTokenGenerator extends TokenGenerator[String] {
    override def toTokens(t: String): Seq[Token] =
      BufferOrEmitStream[Char, nlp.Token](
        t,
        (char, currentBuffer) =>
          char.maybePunctuationToken
            .orElse(char.maybeLineBreakToken)
            .orElse(char.maybeSpaceToken)
            .map {
              case nonWordToken if currentBuffer.isEmpty =>
                nonWordToken :: Nil
              case nonWordToken =>
                val flushedBufferWord =
                  nlp.Token(nlp.Token.Value.Word(nlp.Token.Word(new String(currentBuffer.toArray))))

                flushedBufferWord :: nonWordToken :: Nil
            }
            .map(BufferOrEmitStream.Emit[Char, nlp.Token])
            .getOrElse(BufferOrEmitStream.Buffer[Char, nlp.Token](char)),
        chars => List(new String(chars.toArray).asWordToken)
      )
  }

  implicit case object PhraseWriter extends TokenGenerator[nlp.Phrase] {
    override def toTokens(t: nlp.Phrase): Stream[nlp.Token] =
      t.words
        .map(w => nlp.Token(nlp.Token.Value.Word(w)))
        .intersperse(Constants.SpaceToken)
  }

  implicit case object SentenceWriter extends TokenGenerator[nlp.Sentence] {
    override def toTokens(t: nlp.Sentence): Seq[nlp.Token] =
      t.phrase.toStream.flatMap(_.tokens)
        .append(t.punctuation.map(p => nlp.Token(nlp.Token.Value.Punctuation(p))))
  }

  implicit case object ParagraphWriter extends TokenGenerator[nlp.Paragraph] {
    override def toTokens(t: nlp.Paragraph): Seq[nlp.Token] =
      t.sentences.toStream
        .map(_.tokens)
        .intersperse(List.fill(2)(Constants.SpaceToken))
        .flatten
  }

  implicit case object DocumentWriter extends TokenGenerator[nlp.Document] {
    override def toTokens(t: nlp.Document): Seq[nlp.Token] =
      t.paragraphs.toStream
        .map(_.tokens)
        .intersperse(List.fill(2)(Constants.LineBreakToken))
        .flatten
  }

  implicit case object LineBreakTokenizer extends TokenGenerator[nlp.Token.LineBreak] {
    override def toTokens(t: nlp.Token.LineBreak): Seq[nlp.Token] =
      List(nlp.Token(nlp.Token.Value.LineBreak(t)))
  }

  implicit case object SpaceTokenizer extends TokenGenerator[nlp.Token.Space] {
    override def toTokens(t: nlp.Token.Space): Seq[nlp.Token] =
      List(nlp.Token(nlp.Token.Value.Space(t)))
  }

  implicit case object PunctuationTokenizer extends TokenGenerator[nlp.Token.Punctuation] {
    override def toTokens(t: nlp.Token.Punctuation): Seq[nlp.Token] =
      List(nlp.Token(nlp.Token.Value.Punctuation(t)))
  }

  implicit case object WordTokenizer extends TokenGenerator[nlp.Token.Word] {
    override def toTokens(t: nlp.Token.Word): Seq[nlp.Token] =
      List(nlp.Token(nlp.Token.Value.Word(t)))
  }

}
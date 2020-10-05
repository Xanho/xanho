package org.xanho.nlp.ops


import org.xanho.nlp.Constants
import org.xanho.nlp.ops.NlpCharOps.nlpCharOps
import org.xanho.nlp.ops.TokenSeqOps.tokenSeqOps
import org.xanho.proto.nlp
import org.xanho.proto.nlp.ParseResult

import scala.language.implicitConversions

trait NlpStringOps {
  implicit def nlpStringOps(string: String): NlpString =
    new NlpString(string)
}

object NlpStringOps extends NlpStringOps

class NlpString(val string: String) extends AnyVal {

  import NlpStringOps._

  def sanitize: String =
    string.filter(Constants.ValidCharacters)

  def parse: ParseResult =
    ParseResult()
      .withText(nlp.Text(string))
      .withDocument(tokenStream.asDocument)

  def asWordToken: nlp.Token =
    nlp.Token(nlp.Token.Value.Word(nlp.Token.Word(string)))

  def tokenStream: Stream[nlp.Token] =
    BufferOrEmitStream[Char, nlp.Token](
      string,
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

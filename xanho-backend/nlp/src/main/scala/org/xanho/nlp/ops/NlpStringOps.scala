package org.xanho.nlp.ops


import org.xanho.nlp.Constants
import implicits._
import org.xanho.proto.nlp
import org.xanho.proto.nlp.ParseResult

import scala.language.implicitConversions

trait NlpStringOps {
  implicit def nlpStringOps(string: String): NlpString =
    new NlpString(string)
}

object NlpStringOps extends NlpStringOps

class NlpString(val string: String) extends AnyVal {

  def sanitize: String =
    string.filter(Constants.ValidCharacters)

  def parse: ParseResult =
    ParseResult()
      .withText(nlp.Text(string))
      .withDocument(string.tokens.asDocument)

  def asWordToken: nlp.Token =
    nlp.Token(nlp.Token.Value.Word(nlp.Token.Word(string)))

}

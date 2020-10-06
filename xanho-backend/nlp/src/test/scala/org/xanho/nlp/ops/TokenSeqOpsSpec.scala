package org.xanho.nlp.ops

import org.scalatest.OptionValues
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers
import implicits._

class TokenSeqOpsSpec extends AnyFlatSpecLike with Matchers with OptionValues {

  behavior of "TokenSeqOps"

  it should "interpret a phrase" in {
    val string = "Programming is fun"
    val tokens = string.tokens

    val phrase =
      tokens.asPhrase

    phrase.words.map(_.value) shouldBe List("Programming", "is", "fun")
  }

  it should "interpret a sentence" in {
    val string = "Programming is fun."
    val tokens = string.tokens

    val sentence =
      tokens.asSentence

    sentence.punctuation.value.value shouldBe "."
  }

  it should "interpret a paragraph" in {
    val string = "Programming is fun!  Writing unit tests is not.  Would anyone else like to write them?"
    val tokens = string.tokens

    val paragraph =
      tokens.asParagraph

    val Seq(sentence1, sentence2, sentence3) =
      paragraph.sentences

    sentence1.phrase.value.words.map(_.value).mkString(" ") shouldBe "Programming is fun"
    sentence1.punctuation.value.value shouldBe "!"

    sentence2.phrase.value.words.map(_.value).mkString(" ") shouldBe "Writing unit tests is not"
    sentence2.punctuation.value.value shouldBe "."

    sentence3.phrase.value.words.map(_.value).mkString(" ") shouldBe "Would anyone else like to write them"
    sentence3.punctuation.value.value shouldBe "?"
  }

  it should "interpret a document" in {
    val string =
      """Programming is fun!  Writing unit tests is not.  Would anyone else like to write them?
        |
        |I didn't think so.  No one likes writing unit tests.""".stripMargin
    val tokens = string.tokens

    val document =
      tokens.asDocument

    val Seq(paragraph1, paragraph2) =
      document.paragraphs

    val Seq(sentence1, sentence2, sentence3) =
      paragraph1.sentences

    sentence1.phrase.value.words.map(_.value).mkString(" ") shouldBe "Programming is fun"
    sentence1.punctuation.value.value shouldBe "!"

    sentence2.phrase.value.words.map(_.value).mkString(" ") shouldBe "Writing unit tests is not"
    sentence2.punctuation.value.value shouldBe "."

    sentence3.phrase.value.words.map(_.value).mkString(" ") shouldBe "Would anyone else like to write them"
    sentence3.punctuation.value.value shouldBe "?"

    val Seq(sentence4, sentence5) =
      paragraph2.sentences

    sentence4.phrase.value.words.map(_.value).mkString(" ") shouldBe "I didn't think so"
    sentence4.punctuation.value.value shouldBe "."

    sentence5.phrase.value.words.map(_.value).mkString(" ") shouldBe "No one likes writing unit tests"
    sentence5.punctuation.value.value shouldBe "."

    document.write shouldBe string


  }
}

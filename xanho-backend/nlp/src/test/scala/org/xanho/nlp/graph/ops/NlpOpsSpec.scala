package org.xanho.nlp.graph.ops

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.xanho.proto.graph.Graph
import org.xanho.nlp.graph.ops.implicits._
import org.xanho.nlp.ops.implicits._

class NlpOpsSpec extends AnyFlatSpec with Matchers {

  behavior of "NlpOps"

  it should "build from a document" in {

    val documentString =
      """Scala is a general-purpose programming language providing support for both object-oriented programming and functional programming. The language has a strong static type system. Designed to be concise, many of Scala's design decisions are aimed to address criticisms of Java.
        |
        |Scala source code is intended to be compiled to Java bytecode, so that the resulting executable code runs on a Java virtual machine. Scala provides language interoperability with Java, so that libraries written in either language may be referenced directly in Scala or Java code.[9] Like Java, Scala is object-oriented, and uses a curly-brace syntax reminiscent of the C programming language. Unlike Java, Scala has many features of functional programming languages like Scheme, Standard ML and Haskell, including currying, immutability, lazy evaluation, and pattern matching. It also has an advanced type system supporting algebraic data types, covariance and contravariance, higher-order types (but not higher-rank types), and anonymous types. Other features of Scala not present in Java include operator overloading, optional parameters, named parameters, and raw strings. Conversely, a feature of Java not in Scala is checked exceptions, which has proved controversial.
        |
        |The name Scala is a portmanteau of scalable and language, signifying that it is designed to grow with the demands of its users.
        |""".stripMargin

    val document =
      documentString.parse.document.get

    val (g, documentNode) =
      Graph.defaultInstance.withDocument(document)

    implicit val graph: Graph = g

    val List(paragraph1, paragraph2, paragraph3) =
      documentNode.documentParagraphs.toList

    val List(sentence1, sentence2, sentence3) =
      paragraph1.paragraphSentences.toList

    sentence1.sentencePhrase
      .phraseWords
      .map(_.wordValue)
      .mkString(" ") shouldBe "Scala is a general-purpose programming language providing support for both object-oriented programming and functional programming"

  }

}

package org.xanho.knowledgegraph.actor

import org.xanho.knowledgegraph.actor.implicits.ops.{KnowledgeGraphOps, WordOps}
import org.xanho.proto.knowledgegraphactor.KnowledgeGraphState
import org.xanho.proto.nlp.Token

import scala.language.implicitConversions

package object implicits {

  implicit class KnowledgeGraphImplicits(knowledgeGraphState: KnowledgeGraphState) extends KnowledgeGraphOps(knowledgeGraphState)

  implicit class WordImplicits(word: Token.Word) extends WordOps(word)

  implicit def stringToWord(string: String): Token.Word = Token.Word(string)

}

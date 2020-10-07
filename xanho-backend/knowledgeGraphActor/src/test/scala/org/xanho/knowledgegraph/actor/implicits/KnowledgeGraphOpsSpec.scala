package org.xanho.knowledgegraph.actor.implicits

import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers
import org.xanho.nlp.ops.implicits._
import org.xanho.proto.knowledgegraphactor.KnowledgeGraphState

class KnowledgeGraphOpsSpec extends AnyFlatSpecLike with Matchers {

  behavior of "KnowledgeGraphOpsSpec"

  it should "find sentences containing a word" in {
    val input =
      """
        |Association football, more commonly known as football or soccer,[a] is a team sport played with a spherical ball between two teams of 11 players. It is played by approximately 250 million players in over 200 countries and dependencies, making it the world's most popular sport. The game is played on a rectangular field called a pitch with a goal at each end. The object of the game is to outscore the opposition by moving the ball beyond the goal line into the opposing goal. The team with the higher number of goals wins the game.
        |""".stripMargin

    val parseResult =
      input.parse

    val knowledgeGraphState =
      KnowledgeGraphState(List(parseResult))

    val sentences =
      knowledgeGraphState.sentencesIncludingWord("goal")

    sentences.size shouldBe 2

    val sentences2 =
      knowledgeGraphState.sentencesIncludingOrderedWords(List("goal", "line"))

    sentences2.size shouldBe 1

    val wordFrequencies =
      knowledgeGraphState.wordFrequencies

    wordFrequencies
      .take(3)
      .map(_.word.value) shouldBe Seq("the", "a", "is")

  }

}

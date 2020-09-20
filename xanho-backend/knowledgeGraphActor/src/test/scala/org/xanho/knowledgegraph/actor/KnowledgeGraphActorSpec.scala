package org.xanho.knowledgegraph.actor

import akka.Done
import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.persistence.testkit.scaladsl.EventSourcedBehaviorTestKit
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers
import org.xanho.proto.knowledgegraphactor.{GetState, IngestText, KnowledgeGraphCommand, KnowledgeGraphEvent, KnowledgeGraphState, TextIngested}
import akka.actor.typed.scaladsl.adapter._
import org.xanho.proto.nlp.{Phrase, Sentence, Token}

class KnowledgeGraphActorSpec
  extends ScalaTestWithActorTestKit(EventSourcedBehaviorTestKit.config)
    with AnyFlatSpecLike
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with Matchers {

  private val eventSourcedTestKit =
    EventSourcedBehaviorTestKit[KnowledgeGraphCommand, KnowledgeGraphEvent, KnowledgeGraphState](
      system,
      KnowledgeGraphActor("1")
    )

  override protected def beforeEach(): Unit = {
    super.beforeEach()
    eventSourcedTestKit.clear()
  }

  behavior of "KnowledgeGraphActor"

  it should "parse and persist text input" in {
    val writeResult =
      eventSourcedTestKit.runCommand[Done](replyTo => IngestText(replyTo.toClassic, "Hello world."))

    writeResult.reply shouldBe Done
    writeResult.event shouldBe TextIngested("Hello world.")
    val writeResultState =
      writeResult.stateOfType[KnowledgeGraphState]

    writeResultState.parseResults.head.sentences shouldBe Seq(
      Sentence(
        Some(
          Phrase(
            Seq(Token.Word("Hello"), Token.Word("world"))
          )
        ),
        Some(Token.Punctuation("."))
      )
    )

  }

  it should "return the state from a GetState request" in {
    val writeResult =
      eventSourcedTestKit.runCommand[Done](replyTo => IngestText(replyTo.toClassic, "Hello world."))

    val getStateResult =
      eventSourcedTestKit.runCommand[KnowledgeGraphState](replyTo => GetState(replyTo.toClassic))

    getStateResult.reply shouldBe writeResult.stateOfType[KnowledgeGraphState]
  }

}

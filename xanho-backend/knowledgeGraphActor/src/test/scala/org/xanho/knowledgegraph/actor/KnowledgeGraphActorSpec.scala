package org.xanho.knowledgegraph.actor

import akka.Done
import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.actor.typed.scaladsl.adapter._
import akka.persistence.testkit.{PersistenceTestKitPlugin, PersistenceTestKitSnapshotPlugin}
import akka.persistence.testkit.scaladsl.EventSourcedBehaviorTestKit
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers
import org.xanho.proto.knowledgegraphactor._

class KnowledgeGraphActorSpec
  extends ScalaTestWithActorTestKit(
    PersistenceTestKitPlugin.config
      .withFallback(PersistenceTestKitSnapshotPlugin.config)
      .withFallback(EventSourcedBehaviorTestKit.config)
  )
    with AnyFlatSpecLike
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with Matchers
    with OptionValues {

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

    writeResult.reply shouldBe IngestTextResponse()
    writeResult.event shouldBe TextIngested("Hello world.")
    val writeResultState =
      writeResult.stateOfType[KnowledgeGraphState]

    val Some(document) =
      writeResultState.parseResults.head.document

    val paragraph =
      document.paragraphs.head

    val sentence =
      paragraph.sentences.head

    sentence.phrase.value.words.map(_.value) shouldBe Seq("Hello", "world")

    sentence.punctuation.value.value shouldBe "."

  }

  it should "return the state from a GetState request" in {
    val writeResult =
      eventSourcedTestKit.runCommand[IngestTextResponse](replyTo => IngestText(replyTo.toClassic, "Hello world."))

    val getStateResult =
      eventSourcedTestKit.runCommand[GetStateResponse](replyTo => GetState(replyTo.toClassic))

    getStateResult.reply.state.value shouldBe writeResult.stateOfType[KnowledgeGraphState]
  }

}

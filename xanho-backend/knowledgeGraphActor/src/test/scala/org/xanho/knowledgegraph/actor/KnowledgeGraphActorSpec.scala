package org.xanho.knowledgegraph.actor

import akka.Done
import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.actor.typed.scaladsl.adapter._
import akka.persistence.testkit.scaladsl.EventSourcedBehaviorTestKit
import akka.persistence.testkit.{PersistenceTestKitPlugin, PersistenceTestKitSnapshotPlugin}
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

  private val message =
    TextMessage(
      id = "id1",
      source = MessageSource.USER,
      timestampMs = System.currentTimeMillis(),
      text = "Hello world."
    )

  behavior of "KnowledgeGraphActor"

  it should "parse and persist text and generate a response" in {

    val writeResult =
      eventSourcedTestKit.runCommand[Done](replyTo => IngestTextMessage(replyTo.toClassic, Some(message)))

    writeResult.reply shouldBe IngestTextResponse()
    writeResult.event shouldBe TextMessageIngested(Some(message))
    val writeResultState =
      writeResult.stateOfType[KnowledgeGraphState]

    writeResultState.messages.head shouldBe message

    val getStateResult =
      eventSourcedTestKit.runCommand[GetStateResponse](replyTo => GetState(replyTo.toClassic))

    val state =
      getStateResult.reply.state.value

    state.messages.size shouldBe 2
    state.messages.head shouldBe writeResult.state.messages.head

  }

}

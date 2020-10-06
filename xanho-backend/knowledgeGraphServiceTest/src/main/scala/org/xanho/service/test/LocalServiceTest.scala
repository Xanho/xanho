package org.xanho.service.test

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.grpc.GrpcClientSettings
import akka.stream.scaladsl.Source
import org.slf4j.{Logger, LoggerFactory}
import org.xanho.knowledgegraph.service.proto._
import org.xanho.nlp.ops.TokenizerOps._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.io.StdIn

object LocalServiceTest extends App {

  implicit val system: ActorSystem[_] =
    ActorSystem(Behaviors.empty, "grpc-service-test")

  private val log: Logger =
    LoggerFactory.getLogger(this.getClass)

  val client =
    KnowledgeGraphServiceClient(GrpcClientSettings.connectToServiceAt("localhost", 8080).withTls(false))

  val graphId =
    "graph1"

  while (true) {

    val Some(question) =
      client.generateResponse(GenerateResponseRequest(graphId)).await.document

    println(s"Xanho[$graphId]: ${question.write}")

    val input = StdIn.readLine("You: ")

    val ingestResult =
      client.ingestTextStream(
        Source(
          List(
            IngestTextRequest(
              graphId = graphId,
              text = input
            )
          )
        )
      )
        .await

    val state =
      client.getState(GetStateRequest(graphId = graphId)).await

    log.debug("state={}", state)

    val analysis =
      client.getAnalysis(GetAnalysisRequest(graphId = graphId)).await

    log.debug("analysis={}", analysis)
  }

  system.terminate()

  implicit class FutureHelpers[T](future: Future[T]) {
    def await: T =
      future.await(20.seconds)

    def await(duration: Duration): T =
      Await.result(future, duration)
  }

}

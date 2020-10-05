package org.xanho.service.test

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.grpc.GrpcClientSettings
import akka.stream.scaladsl.Source
import org.xanho.knowledgegraph.service.proto.{GetAnalysisRequest, GetStateRequest, IngestTextRequest, KnowledgeGraphServiceClient}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object LocalServiceTest extends App {

  implicit val system: ActorSystem[_] =
    ActorSystem(Behaviors.empty, "grpc-service-test")

  val client =
    KnowledgeGraphServiceClient(GrpcClientSettings.connectToServiceAt("localhost", 8080).withTls(false))

  val ingestResult =
    client.ingestTextStream(
      Source(
        List(
          IngestTextRequest(
            graphId = "graph1",
            text = "Hello world."
          )
        )
      )
    )
      .await

  val state =
    client.getState(GetStateRequest(graphId = "graph1")).await

  println(state)

  val analysis =
    client.getAnalysis(GetAnalysisRequest(graphId = "graph1")).await

  println(analysis)

  system.terminate()

  implicit class FutureHelpers[T](future: Future[T]) {
    def await: T =
      future.await(20.seconds)

    def await(duration: Duration): T =
      Await.result(future, duration)
  }

}

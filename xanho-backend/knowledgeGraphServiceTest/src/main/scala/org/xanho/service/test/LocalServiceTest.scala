package org.xanho.service.test

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.grpc.GrpcClientSettings
import akka.stream.scaladsl.Source
import org.xanho.knowledgegraph.service.proto.{GetStateRequest, GetStateResponse, IngestTextRequest, KnowledgeGraphServiceClient}

import scala.concurrent.Await
import scala.concurrent.duration._

object LocalServiceTest extends App {

  implicit val system: ActorSystem[_] =
    ActorSystem(Behaviors.empty, "grpc-service-test")

  import system.executionContext

  val client =
    KnowledgeGraphServiceClient(GrpcClientSettings.connectToServiceAt("localhost", 8080).withTls(false))

  val stateF =
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
      .flatMap(_ =>
        client.getState(GetStateRequest(graphId = "graph1"))
      )

  val state: GetStateResponse =
    Await.result(stateF, 20.seconds)

  println(state)

  system.terminate()

}

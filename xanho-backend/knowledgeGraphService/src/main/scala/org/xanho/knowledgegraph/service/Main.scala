package org.xanho.knowledgegraph.service

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

import scala.concurrent.Await
import scala.concurrent.duration._

object Main extends App {
  val system: ActorSystem[Nothing] =
    ActorSystem(Behaviors.empty, "knowledge-graph-service")

  Await.result(
    KnowledgeGraphGrpcServer(system)
      .start(),
    10.seconds
  )

}

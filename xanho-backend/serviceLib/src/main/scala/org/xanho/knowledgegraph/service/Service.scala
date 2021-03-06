package org.xanho.knowledgegraph.service

import akka.Done
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

trait Service {

  implicit val system: ActorSystem[_] =
    ActorSystem(Behaviors.empty, "xanho-backend")

  import system.executionContext

  protected def preStart(): Future[Done]

  protected def run(): Unit = {
    val f =
      for {
        _ <- preStart()
        _ <- startAppServer()
        _ <- startHealthcheckServer()
      } yield Done

    Await.result(f, 30.seconds)
  }

  protected def startHealthcheckServer(): Future[Done] = {
    val server = HealthcheckServerExt(system).server
    server.start()
  }

  protected def startAppServer(): Future[Done] = {
    val server = new GrpcServer()

    val grpcWebServer = new GrpcWebServer()

    for {
      _ <- server.start()
      _ <- grpcWebServer.start()
    } yield Done
  }

}

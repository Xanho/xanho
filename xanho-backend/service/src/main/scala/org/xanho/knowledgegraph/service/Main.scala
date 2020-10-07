package org.xanho.knowledgegraph.service

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

object Main extends App {

  implicit val system: ActorSystem[_] =
    ActorSystem(Behaviors.empty, "xanho-service")

  val server = new GrpcServer()

  server.start()

}

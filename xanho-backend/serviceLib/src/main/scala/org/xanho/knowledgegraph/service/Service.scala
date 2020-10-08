package org.xanho.knowledgegraph.service

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

trait Service {

  implicit val system: ActorSystem[_] =
    ActorSystem(Behaviors.empty, "xanho-backend")

  val server = new GrpcServer()

  server.start()

}

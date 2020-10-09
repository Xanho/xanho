package org.xanho.backend

import akka.Done
import org.xanho.knowledgegraph.service.Service

import scala.concurrent.Future

object LocalApp extends App with Service {
  override protected def preStart(): Future[Done] =
    Future.successful(Done)

  run()
}

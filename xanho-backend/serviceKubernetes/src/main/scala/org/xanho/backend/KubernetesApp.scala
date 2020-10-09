package org.xanho.backend

import akka.Done
import akka.management.cluster.bootstrap.ClusterBootstrap
import akka.management.scaladsl.AkkaManagement
import org.xanho.knowledgegraph.service.Service

import scala.concurrent.Future

object KubernetesApp extends App with Service {

  import system.executionContext

  override protected def preStart(): Future[Done] =
    AkkaManagement(system).start()
      .map(_ =>
        ClusterBootstrap(system).start()
      )
      .map(_ => Done)

  run()
}

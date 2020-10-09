package org.xanho.backend

import akka.management.cluster.bootstrap.ClusterBootstrap
import akka.management.scaladsl.AkkaManagement
import org.xanho.knowledgegraph.service.Service

object KubernetesApp extends App with Service {
  import system.executionContext

  AkkaManagement(system).start()
    .map(_ =>
      ClusterBootstrap(system).start()
    )
}

package org.xanho.knowledgegraph.service

import akka.Done
import akka.actor.CoordinatedShutdown
import akka.actor.typed.{ActorSystem, Extension}
import akka.http.scaladsl.Http
import org.slf4j.{Logger, LoggerFactory}
import org.xanho.knowledgegraph.service.proto.KnowledgeGraphServiceHandler

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

class GrpcServer(implicit system: ActorSystem[_]) extends Extension {

  import system.executionContext

  private val log: Logger = LoggerFactory.getLogger(this.getClass)

  private var started: Boolean =
    false

  private val bindingPromise: Promise[Http.ServerBinding] =
    Promise()

  private val grpcImpl =
    new GrpcService()

  private val handler =
    KnowledgeGraphServiceHandler(grpcImpl)

  private val bindingHost =
    system.settings.config.getString("service.binding.host")

  private val bindingPort: Int =
    system.settings.config.getInt("service.binding.port")

  def start(): Future[Done] = {
    log.info("Binding gRPC Service. host={} port={}", bindingHost, bindingPort)
    started = true
    bindingPromise.completeWith(
      Http()(system.classicSystem)
        .newServerAt(bindingHost, bindingPort)
        .bind(handler)
    )
      .future
      .andThen {
        case Success(_) =>
          log.info("gRPC Service Bound Successfully. host={} port={}", bindingHost, bindingPort)
          CoordinatedShutdown(system)
            .addCancellableTask(CoordinatedShutdown.PhaseServiceUnbind, "shutdown-grpc-server")(stop)
        case Failure(exception) =>
          log.error("gRPC Service failed to start.", exception)
      }
      .map(_ => Done)
  }

  def stop(): Future[Done] = {
    if (started) {
      log.info("Unbinding gRPC Service. host={} port={}", bindingHost, bindingPort)
      bindingPromise.future
        .flatMap(_.unbind())
        .andThen {
          case Success(_) =>
            log.info("gRPC Service Unbound Successfully. host={} port={}", bindingHost, bindingPort)
          case Failure(exception) =>
            log.error("gRPC Service failed to unbind.", exception)
        }
    } else {
      Future.failed(new IllegalStateException("Service not started"))
    }
  }

}

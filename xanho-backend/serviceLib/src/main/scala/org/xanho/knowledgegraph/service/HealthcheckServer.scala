package org.xanho.knowledgegraph.service

import akka.Done
import akka.actor.CoordinatedShutdown
import akka.actor.typed.{ActorSystem, Extension}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

class HealthcheckServer(implicit system: ActorSystem[_]) extends Extension {

  import system.executionContext

  private val log: Logger = LoggerFactory.getLogger(this.getClass)

  private var started: Boolean =
    false

  private val bindingPromise: Promise[Http.ServerBinding] =
    Promise()

  private val route =
    pathSingleSlash {
      get {
        complete(StatusCodes.OK)
      }
    }

  private val bindingHost =
    system.settings.config.getString("healthcheck.binding.host")

  private val bindingPort: Int =
    system.settings.config.getInt("healthcheck.binding.port")

  def start(): Future[Done] = {
    log.info("Binding Healthcheck Service. host={} port={}", bindingHost, bindingPort)
    started = true
    bindingPromise.completeWith(
      Http()(system.classicSystem)
        .newServerAt(bindingHost, bindingPort)
        .bind(route)
    )
      .future
      .andThen {
        case Success(_) =>
          log.info("Healthcheck Service Bound Successfully. host={} port={}", bindingHost, bindingPort)
          CoordinatedShutdown(system)
            .addCancellableTask(CoordinatedShutdown.PhaseServiceUnbind, "shutdown-healthcheck-server")(stop)
        case Failure(exception) =>
          log.error("Healthcheck Service failed to start.", exception)
      }
      .map(_ => Done)
  }

  def stop(): Future[Done] = {
    if (started) {
      log.info("Unbinding Healthcheck Service. host={} port={}", bindingHost, bindingPort)
      bindingPromise.future
        .flatMap(_.unbind())
        .andThen {
          case Success(_) =>
            log.info("Healthcheck Service Unbound Successfully. host={} port={}", bindingHost, bindingPort)
          case Failure(exception) =>
            log.error("Healthcheck Service failed to unbind.", exception)
        }
    } else {
      Future.failed(new IllegalStateException("Service not started"))
    }
  }

}

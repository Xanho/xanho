package org.xanho.knowledgegraph.service

import akka.Done
import akka.actor.CoordinatedShutdown
import akka.actor.typed.{ActorSystem, Extension, ExtensionId}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

class HealthcheckServerExt(implicit system: ActorSystem[_]) extends Extension {

  private val bindingHost =
    system.settings.config.getString("healthcheck.binding.host")

  private val bindingPort: Int =
    system.settings.config.getInt("healthcheck.binding.port")

  val server =
    new HealthcheckServer(bindingHost, bindingPort)

}

object HealthcheckServerExt extends ExtensionId[HealthcheckServerExt] {
  override def createExtension(system: ActorSystem[_]): HealthcheckServerExt =
    new HealthcheckServerExt()(system)

}

class HealthcheckServer(bindingHost: String, bindingPort: Int)(implicit system: ActorSystem[_]) {

  import system.executionContext

  private val log: Logger = LoggerFactory.getLogger(this.getClass)

  private var started: Boolean =
    false

  private val bindingPromise: Promise[Http.ServerBinding] =
    Promise()

  private val route =
    get {
      complete(StatusCodes.OK)
    }

  def start(): Future[Done] = {
    log.info("Binding Healthcheck Service. host={} port={}", bindingHost, bindingPort)
    started = true
    val serverBuilder =
      XanhoHttps(system).contextOpt
        .foldLeft(
          Http()(system.classicSystem).newServerAt(bindingHost, bindingPort)
        )((server, https) => server.enableHttps(https))
    bindingPromise.completeWith(
      serverBuilder.bind(route)
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
package org.xanho.knowledgegraph.service

import akka.Done
import akka.actor.CoordinatedShutdown
import akka.actor.typed.{ActorSystem, Extension}
import akka.grpc.scaladsl.WebHandler
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.Uri.Path
import org.slf4j.{Logger, LoggerFactory}
import org.xanho.proto.service.knowledgegraph.KnowledgeGraphServiceHandler

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

class GrpcWebServer(implicit system: ActorSystem[_]) extends Extension {

  import system.executionContext

  private val log: Logger = LoggerFactory.getLogger(this.getClass)

  private var started: Boolean =
    false

  private val bindingPromise: Promise[Http.ServerBinding] =
    Promise()

  private val grpcImpl =
    new GrpcService()

  private val handler = {
    val f =
      (rewriteRequestPath _)
        .andThen(KnowledgeGraphServiceHandler(grpcImpl))
    WebHandler.grpcWebHandler({ case r => f(r) })
  }

  private def rewriteRequestPath(httpRequest: HttpRequest): HttpRequest =
    httpRequest.withUri(
      httpRequest.uri.path match {
        case Path.Slash(Path.Segment("grpc-web", tail)) =>
          httpRequest.uri.withPath(tail)
        case _ =>
          httpRequest.uri
      }
    )

  private val bindingHost =
    system.settings.config.getString("grpc-web-service.binding.host")

  private val bindingPort: Int =
    system.settings.config.getInt("grpc-web-service.binding.port")

  def start(): Future[Done] = {
    log.info("Binding gRPC-web Service. host={} port={}", bindingHost, bindingPort)
    started = true
    val serverBuilder =
      XanhoHttps(system).contextOpt
        .foldLeft(
          Http()(system.classicSystem).newServerAt(bindingHost, bindingPort)
        )(_.enableHttps(_))
    bindingPromise.completeWith(
      serverBuilder.bind(handler)
    )
      .future
      .andThen {
        case Success(_) =>
          log.info("gRPC-web Service Bound Successfully. host={} port={}", bindingHost, bindingPort)
          CoordinatedShutdown(system)
            .addCancellableTask(CoordinatedShutdown.PhaseServiceUnbind, "shutdown-grpc-web-server")(stop)
        case Failure(exception) =>
          log.error("gRPC-web Service failed to start.", exception)
      }
      .map(_ => Done)
  }

  def stop(): Future[Done] = {
    if (started) {
      log.info("Unbinding gRPC-web Service. host={} port={}", bindingHost, bindingPort)
      bindingPromise.future
        .flatMap(_.unbind())
        .andThen {
          case Success(_) =>
            log.info("gRPC-web Service Unbound Successfully. host={} port={}", bindingHost, bindingPort)
          case Failure(exception) =>
            log.error("gRPC-web Service failed to unbind.", exception)
        }
    } else {
      Future.failed(new IllegalStateException("Service not started"))
    }
  }

}

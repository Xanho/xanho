package org.xanho.knowledgegraph.service.streamlets

import akka.Done
import akka.actor.CoordinatedShutdown
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.util.Timeout
import cloudflow.akkastream.{AkkaServerStreamlet, AkkaStreamletLogic, Clustering}
import cloudflow.streamlets.StreamletShape
import org.xanho.knowledgegraph.service.proto._

import scala.concurrent.duration._
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

class GrpcStreamlet extends AkkaServerStreamlet with Clustering {

  private implicit val askTimeout: Timeout =
    Timeout(10.seconds)

  private val bindingHost = "0.0.0.0"

  override protected def createLogic(): AkkaStreamletLogic =
    new AkkaStreamletLogic() {

      private val bindingPromise: Promise[Http.ServerBinding] =
        Promise()

      override def run(): Unit = {

        val grpcImpl =
          new GrpcService()(system.toTyped)

        val handler =
          KnowledgeGraphServiceHandler(grpcImpl)

        start(handler)
      }

      def start(handler: HttpRequest => Future[HttpResponse]): Future[Done] = {
        log.info("Binding gRPC Service. host={} port={}", bindingHost, containerPort)
        bindingPromise.completeWith(
          Http()(system)
            .newServerAt("0.0.0.0", containerPort)
            .bind(handler)
        )
          .future
          .andThen {
            case Success(value) =>
              log.info("gRPC Service Bound Successfully. host={} port={}", bindingHost, containerPort)
              CoordinatedShutdown(system)
                .addCancellableTask(CoordinatedShutdown.PhaseServiceUnbind, "shutdown-grpc-server")(stop)
            case Failure(exception) =>
              log.error("gRPC Service failed to start.", exception)
          }
          .map(_ => Done)
      }

      def stop(): Future[Done] = {
        log.info("Unbinding gRPC Service. host={} port={}", bindingHost, containerPort)
        bindingPromise.future
          .flatMap(_.unbind())
          .andThen {
            case Success(_) =>
              log.info("gRPC Service Unbound Successfully. host={} port={}", bindingHost, containerPort)
              CoordinatedShutdown(system)
                .addCancellableTask(CoordinatedShutdown.PhaseServiceUnbind, "shutdown-grpc-server")(stop)
            case Failure(exception) =>
              log.error("gRPC Service failed to unbind.", exception)
          }
      }
    }

  override def shape(): StreamletShape = StreamletShape.empty

}

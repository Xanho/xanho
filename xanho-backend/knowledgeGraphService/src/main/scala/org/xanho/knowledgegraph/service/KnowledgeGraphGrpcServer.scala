package org.xanho.knowledgegraph.service

import akka.actor.CoordinatedShutdown
import akka.{Done, NotUsed}
import akka.actor.typed.{ActorRef, ActorSystem, Extension, ExtensionId}
import akka.cluster.sharding.typed.ShardingEnvelope
import akka.cluster.sharding.typed.scaladsl.{ClusterSharding, Entity, EntityTypeKey}
import akka.stream.scaladsl.Source
import org.xanho.knowledgegraph.actor.KnowledgeGraphActor
import org.xanho.knowledgegraph.service.proto.{GetStateRequest, GetStateResponse, IngestTextRequest, IngestTextStreamResponse, KnowledgeGraphService, KnowledgeGraphServiceHandler}
import org.xanho.proto.knowledgegraphactor.{GetState, IngestText, KnowledgeGraphCommand, KnowledgeGraphState}
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.Http
import akka.http.scaladsl.HttpConnectionContext
import akka.util.Timeout
import org.slf4j.LoggerFactory

import scala.concurrent.{Future, Promise}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class KnowledgeGraphGrpcServer(implicit system: ActorSystem[_]) extends Extension {

  import system.executionContext

  private val logger =
    LoggerFactory.getLogger(this.getClass)

  private val bindingHost =
    system.settings.config.getString("binding.host")

  private val bindingPort =
    system.settings.config.getInt("binding.port")

  private val bindingPromise: Promise[Http.ServerBinding] =
    Promise()

  private val handler =
    KnowledgeGraphServiceHandler(
      new KnowledgeGraphServiceImpl()
    )

  def start(): Future[Done] = {
    logger.info("Binding gRPC Service. host={} port={}", bindingHost, bindingPort)
    bindingPromise.completeWith(
      Http()(system.toClassic)
        .bindAndHandleAsync(
          handler,
          interface = bindingHost,
          port = bindingPort,
          connectionContext = HttpConnectionContext()
        )
    )
      .future
      .andThen {
        case Success(value) =>
          logger.info("gRPC Service Bound Successfully. host={} port={}", bindingHost, bindingPort)
          CoordinatedShutdown(system)
            .addCancellableTask(CoordinatedShutdown.PhaseServiceUnbind, "shutdown-grpc-server")(stop)
        case Failure(exception) =>
          logger.error("gRPC Service failed to start.", exception)
      }
      .map(_ => Done)
  }

  def stop(): Future[Done] = {
    logger.info("Unbinding gRPC Service. host={} port={}", bindingHost, bindingPort)
    bindingPromise.future
      .flatMap(_.unbind())
      .andThen {
        case Success(_) =>
          logger.info("gRPC Service Unbound Successfully. host={} port={}", bindingHost, bindingPort)
          CoordinatedShutdown(system)
            .addCancellableTask(CoordinatedShutdown.PhaseServiceUnbind, "shutdown-grpc-server")(stop)
        case Failure(exception) =>
          logger.error("gRPC Service failed to unbind.", exception)
      }
  }


}

object KnowledgeGraphGrpcServer extends ExtensionId[KnowledgeGraphGrpcServer] {
  override def createExtension(system: ActorSystem[_]): KnowledgeGraphGrpcServer =
    new KnowledgeGraphGrpcServer()(system)
}

class KnowledgeGraphServiceImpl(implicit system: ActorSystem[_]) extends KnowledgeGraphService {

  import system.executionContext

  private val sharding: ClusterSharding = ClusterSharding(system)

  private val KnowledgeGraphActorTypeKey = EntityTypeKey[KnowledgeGraphCommand]("KnowledgeGraphCommand")

  private val shardRegion: ActorRef[ShardingEnvelope[KnowledgeGraphCommand]] =
    sharding.init(Entity(KnowledgeGraphActorTypeKey)(createBehavior = entityContext => KnowledgeGraphActor(entityContext.entityId)))

  private implicit val askTimeout: Timeout =
    Timeout(10.seconds)

  override def ingestTextStream(in: Source[IngestTextRequest, NotUsed]): Future[IngestTextStreamResponse] =
    in.mapAsyncUnordered(10)(request =>
      sharding.entityRefFor(KnowledgeGraphActorTypeKey, request.graphId).ask[Done](
        ref => IngestText(ref.toClassic, request.text)
      )
    )
      .runFold(0)((c, _) => c + 1)
      .map(IngestTextStreamResponse(_))

  override def getState(request: GetStateRequest): Future[GetStateResponse] = {
    sharding.entityRefFor(KnowledgeGraphActorTypeKey, request.graphId).ask[KnowledgeGraphState](
      ref => GetState(ref.toClassic)
    )
      .map(state => GetStateResponse(request.graphId, Some(state)))
  }

}
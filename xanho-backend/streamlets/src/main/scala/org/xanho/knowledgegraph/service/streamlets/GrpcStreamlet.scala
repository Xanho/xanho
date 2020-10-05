package org.xanho.knowledgegraph.service.streamlets

import akka.actor.CoordinatedShutdown
import akka.actor.typed.scaladsl.adapter._
import akka.cluster.sharding.typed.scaladsl.{ClusterSharding, Entity, EntityTypeKey}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.Materializer
import akka.stream.scaladsl.{Keep, MergeHub, Sink, Source}
import akka.util.Timeout
import akka.{Done, NotUsed}
import cloudflow.akkastream.{AkkaServerStreamlet, AkkaStreamletLogic, Clustering}
import cloudflow.streamlets.proto.ProtoOutlet
import cloudflow.streamlets.{RoundRobinPartitioner, StreamletShape}
import org.xanho.knowledgegraph.actor.KnowledgeGraphActor
import org.xanho.knowledgegraph.actor.implicits._
import org.xanho.knowledgegraph.service.proto._
import org.xanho.proto.knowledgegraphactor.{GetState, KnowledgeGraphCommand, KnowledgeGraphState}
import org.xanho.proto.nlp

import scala.concurrent.duration._
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

class GrpcStreamlet extends AkkaServerStreamlet with Clustering {

  val outlet: ProtoOutlet[IngestTextRequest] =
    ProtoOutlet[IngestTextRequest]("out", RoundRobinPartitioner(_))

  private implicit val askTimeout: Timeout =
    Timeout(10.seconds)

  private val bindingHost = "0.0.0.0"

  override protected def createLogic(): AkkaStreamletLogic =
    new AkkaStreamletLogic() {

      private val bindingPromise: Promise[Http.ServerBinding] =
        Promise()

      override def run(): Unit = {

        val sharding: ClusterSharding =
          ClusterSharding(system.toTyped)

        val KnowledgeGraphActorTypeKey =
          EntityTypeKey[KnowledgeGraphCommand]("KnowledgeGraphCommand")

        sharding.init(Entity(KnowledgeGraphActorTypeKey)(createBehavior = entityContext => KnowledgeGraphActor(entityContext.entityId)))

        val reusableSink: Sink[IngestTextRequest, NotUsed] =
          MergeHub.source[IngestTextRequest]
            .toMat(plainSink(outlet))(Keep.left)
            .run()

        val getStateImpl =
          (graphId: String) =>
            sharding.entityRefFor(KnowledgeGraphActorTypeKey, graphId).ask[GetStateResponse](
              ref => GetState(ref.toClassic)
            )
              .map(_.state.get) // TODO: None.get

        val grpcImpl =
          new KnowledgeGraphServiceStreamletImpl(reusableSink, getStateImpl)

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

  override def shape(): StreamletShape = StreamletShape(outlet)

}

private class KnowledgeGraphServiceStreamletImpl(ingestTextSink: Sink[IngestTextRequest, _], getStateImpl: String => Future[KnowledgeGraphState])(implicit mat: Materializer) extends KnowledgeGraphService {

  import mat.executionContext

  override def ingestTextStream(in: Source[IngestTextRequest, NotUsed]): Future[IngestTextStreamResponse] =
    in
      .alsoTo(ingestTextSink)
      .runFold(0)((c, _) => c + 1)
      .map(IngestTextStreamResponse(_))

  override def getState(in: GetStateRequest): Future[GetStateResponse] =
    getStateImpl(in.graphId)
      .map(state => GetStateResponse(in.graphId, Some(state)))

  override def getAnalysis(in: GetAnalysisRequest): Future[GetAnalysisResponse] =
    getStateImpl(in.graphId)
      .map(state =>
        GetAnalysisResponse(in.graphId)
          .withVocabulary(state.vocabulary.toList)
          .withWordFrequencies(state.wordFrequencies.map(wordFrequency => GetAnalysisResponse.WordFrequency(Some(wordFrequency.word), wordFrequency.percent)))
      )

  override def generateResponse(in: GenerateResponseRequest): Future[GenerateResponseResponse] =
    getStateImpl(in.graphId)
      .map(state =>
        GenerateResponseResponse(
          in.graphId,
          Some(nlp.Document(
            List(nlp.Paragraph(
              List(nlp.Sentence(
                Some(nlp.Phrase(
                  List(nlp.Token.Word("Why"))
                )),
                Some(nlp.Token.Punctuation("?"))
              ))
            ))
          ))
        )
      )
}

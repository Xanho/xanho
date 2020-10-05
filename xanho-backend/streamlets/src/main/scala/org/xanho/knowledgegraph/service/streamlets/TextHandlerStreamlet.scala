package org.xanho.knowledgegraph.service.streamlets

import akka.actor.typed.scaladsl.adapter._
import akka.cluster.sharding.typed.scaladsl.{ClusterSharding, Entity, EntityTypeKey}
import akka.util.Timeout
import cloudflow.akkastream.{AkkaStreamlet, AkkaStreamletLogic, Clustering}
import cloudflow.streamlets.StreamletShape
import cloudflow.streamlets.proto.ProtoInlet
import org.xanho.knowledgegraph.actor.KnowledgeGraphActor
import org.xanho.knowledgegraph.service.proto.IngestTextRequest
import org.xanho.proto.knowledgegraphactor.{IngestText, IngestTextResponse, KnowledgeGraphCommand}

import scala.concurrent.duration._

class TextHandlerStreamlet extends AkkaStreamlet with Clustering {

  val inlet: ProtoInlet[IngestTextRequest] =
    ProtoInlet[IngestTextRequest]("in")

  private implicit val askTimeout: Timeout =
    Timeout(10.seconds)

  override protected def createLogic(): AkkaStreamletLogic =
    new AkkaStreamletLogic() {
      override def run(): Unit = {

        val sharding: ClusterSharding =
          ClusterSharding(system.toTyped)

        val KnowledgeGraphActorTypeKey =
          EntityTypeKey[KnowledgeGraphCommand]("KnowledgeGraphCommand")

        sharding.init(Entity(KnowledgeGraphActorTypeKey)(createBehavior = entityContext => KnowledgeGraphActor(entityContext.entityId)))

        sourceWithCommittableContext(inlet)
          .mapAsync(10)(request =>
            sharding.entityRefFor(KnowledgeGraphActorTypeKey, request.graphId).ask[IngestTextResponse](
              ref => IngestText(ref.toClassic, request.text)
            )
          )
          .runWith(committableSink)
      }
    }

  override def shape(): StreamletShape = StreamletShape(inlet)
}

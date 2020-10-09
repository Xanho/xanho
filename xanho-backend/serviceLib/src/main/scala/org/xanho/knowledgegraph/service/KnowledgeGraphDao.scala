package org.xanho.knowledgegraph.service

import akka.Done
import akka.actor.typed.{ActorSystem, Extension, ExtensionId}
import akka.actor.typed.scaladsl.adapter.TypedActorRefOps
import akka.cluster.sharding.typed.scaladsl.{ClusterSharding, Entity, EntityTypeKey}
import akka.util.Timeout
import org.xanho.knowledgegraph.actor.KnowledgeGraphActor
import org.xanho.knowledgegraph.actor.implicits.KnowledgeGraphImplicits
import org.xanho.proto.service.knowledgegraph.GetAnalysisResponse
import org.xanho.proto.knowledgegraphactor.{IngestText, IngestTextResponse, KnowledgeGraphCommand}
import org.xanho.proto.{nlp, knowledgegraphactor => kgaProtos}

import scala.concurrent.Future
import scala.concurrent.duration._

class KnowledgeGraphDao(implicit system: ActorSystem[_]) extends Extension {

  import system.executionContext

  private implicit val askTimeout: Timeout =
    Timeout(10.seconds)

  private val sharding: ClusterSharding =
    ClusterSharding(system)

  private val KnowledgeGraphActorTypeKey: EntityTypeKey[KnowledgeGraphCommand] =
    EntityTypeKey[KnowledgeGraphCommand]("KnowledgeGraphCommand")

  sharding.init(Entity(KnowledgeGraphActorTypeKey)(createBehavior = entityContext => KnowledgeGraphActor(entityContext.entityId)))

  def tellGraph(graphId: String, text: String): Future[Done] =
    entityRef(graphId)
      .ask[IngestTextResponse](
        ref => IngestText(ref.toClassic, text)
      )
      .map(_ => Done)

  def getState(graphId: String): Future[kgaProtos.KnowledgeGraphState] =
    entityRef(graphId)
      .ask[kgaProtos.GetStateResponse](
        ref => kgaProtos.GetState(ref.toClassic)
      )
      .map(_.state.get) // TODO: None.get

  def getAnalysis(graphId: String): Future[GetAnalysisResponse] =
    getState(graphId)
      .map(state =>
        GetAnalysisResponse(graphId)
          .withVocabulary(state.vocabulary.toList)
          .withWordFrequencies(
            state.wordFrequencies
              .map(wordFrequency => GetAnalysisResponse.WordFrequency(Some(wordFrequency.word), wordFrequency.percent))
          )
      )

  def generateResponse(graphId: String): Future[nlp.Document] =
    entityRef(graphId)
      .ask[kgaProtos.GenerateResponseResponse](
        ref => kgaProtos.GenerateResponse(ref.toClassic)
      )
      .map(_.document.get) // TODO: None.get

  private def entityRef(graphId: String) =
    sharding.entityRefFor(KnowledgeGraphActorTypeKey, graphId)

}

object KnowledgeGraphDao extends ExtensionId[KnowledgeGraphDao] {
  override def createExtension(system: ActorSystem[_]): KnowledgeGraphDao =
    new KnowledgeGraphDao()(system)
}

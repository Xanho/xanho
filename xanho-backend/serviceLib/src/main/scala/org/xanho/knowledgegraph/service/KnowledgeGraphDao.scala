package org.xanho.knowledgegraph.service

import akka.NotUsed
import akka.actor.typed.scaladsl.adapter.TypedActorRefOps
import akka.actor.typed.{ActorSystem, Extension, ExtensionId}
import akka.cluster.sharding.typed.scaladsl.{ClusterSharding, Entity, EntityTypeKey}
import akka.stream.scaladsl.Source
import akka.util.Timeout
import org.xanho.knowledgegraph.actor.KnowledgeGraphActor
import org.xanho.proto.knowledgegraphactor.TextMessageIngested
import org.xanho.proto.service.knowledgegraph.TextMessage
import org.xanho.proto.service.{knowledgegraph => serviceProtos}
import org.xanho.proto.{knowledgegraphactor => kgaProtos}

import scala.concurrent.Future
import scala.concurrent.duration._

class KnowledgeGraphDao(implicit system: ActorSystem[_]) extends Extension {

  import system.executionContext

  private implicit val askTimeout: Timeout =
    Timeout(10.seconds)

  private val sharding: ClusterSharding =
    ClusterSharding(system)

  private val KnowledgeGraphActorTypeKey: EntityTypeKey[kgaProtos.KnowledgeGraphCommand] =
    EntityTypeKey[kgaProtos.KnowledgeGraphCommand]("KnowledgeGraphCommand")

  private val readJournal =
    PersistenceReadJournal(system)()

  sharding.init(Entity(KnowledgeGraphActorTypeKey)(createBehavior = entityContext => KnowledgeGraphActor(entityContext.entityId)))

  def sendMessage(graphId: String, message: serviceProtos.TextMessage): Future[Boolean] =
    entityRef(graphId)
      .ask[kgaProtos.IngestTextResponse](
        ref => kgaProtos.IngestTextMessage(ref.toClassic, Some(
          kgaProtos.TextMessage(
            id = message.id,
            source = kgaProtos.MessageSource.USER,
            timestampMs = message.timestampMs,
            text = message.text
          )
        ))
      )
      .map(_.success)

  def getState(graphId: String): Future[kgaProtos.KnowledgeGraphState] =
    entityRef(graphId)
      .ask[kgaProtos.GetStateResponse](
        ref => kgaProtos.GetState(ref.toClassic)
      )
      .map(_.state.get) // TODO: None.get

  def generateMessage(graphId: String): Future[serviceProtos.TextMessage] =
    entityRef(graphId)
      .ask[kgaProtos.GenerateMessageResponse](
        ref => kgaProtos.GenerateMessage(ref.toClassic)
      )
      .map(_.message.get) // TODO: None.get
      .map(message =>
        serviceProtos.TextMessage(
          id = message.id,
          source = serviceProtos.MessageSource.fromValue(message.source.value),
          timestampMs = message.timestampMs,
          text = message.text
        )
      )

  def messagesStream(graphId: String): Source[TextMessage, NotUsed] =
    readJournal.eventsByPersistenceId(
      graphId,
      0L,
      Long.MaxValue
    )
      .map(_.event)
      .collect {
        case TextMessageIngested(Some(message), _) =>
          serviceProtos.TextMessage(
            id = message.id,
            source = serviceProtos.MessageSource.fromValue(message.source.value),
            timestampMs = message.timestampMs,
            text = message.text
          )
      }

  private def entityRef(graphId: String) =
    sharding.entityRefFor(KnowledgeGraphActorTypeKey, graphId)

}

object KnowledgeGraphDao extends ExtensionId[KnowledgeGraphDao] {
  override def createExtension(system: ActorSystem[_]): KnowledgeGraphDao =
    new KnowledgeGraphDao()(system)
}

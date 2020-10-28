package org.xanho.knowledgegraph.service

import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.stream.scaladsl.Source
import org.xanho.nlp.graph.ops.implicits.nlpGraphBuilderOps
import org.xanho.proto.graph.Graph
import org.xanho.proto.service.knowledgegraph._

import scala.concurrent.Future

class GrpcService()(implicit system: ActorSystem[_]) extends KnowledgeGraphService {

  private val knowledgeGraphDao =
    KnowledgeGraphDao(system)

  import system.executionContext

  override def messagesStream(in: MessagesStreamRequest): Source[TextMessage, NotUsed] =
    knowledgeGraphDao.messagesStream(in.graphId)

  override def sendTextMessage(in: SendTextMessageRequest): Future[SendTextMessageResponse] =
    Future.successful(in.message)
      .flatMap {
        case Some(message) =>
          knowledgeGraphDao.sendMessage(in.graphId, message)
            .map(SendTextMessageResponse(in.graphId, _))
        case _ =>
          Future.failed(new IllegalArgumentException())
      }

  override def getGraph(in: GetGraphRequest): Future[GetGraphResponse] =
    knowledgeGraphDao
      .getState(in.graphId)
      .map(_.graph.orElse(Some(Graph.defaultInstance)))
      .map(GetGraphResponse(in.graphId, _))
}

package org.xanho.knowledgegraph.service

import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.stream.scaladsl.Source
import org.xanho.proto.service.knowledgegraph.{GenerateResponseRequest, GenerateResponseResponse, GetAnalysisRequest, GetAnalysisResponse, IngestTextRequest, IngestTextStreamResponse, KnowledgeGraphService}

import scala.concurrent.Future

class GrpcService()(implicit system: ActorSystem[_]) extends KnowledgeGraphService {

  private val knowledgeGraphDao =
    KnowledgeGraphDao(system)

  import system.executionContext

  override def ingestTextStream(in: Source[IngestTextRequest, NotUsed]): Future[IngestTextStreamResponse] =
    in
      .mapAsync(1)(request =>
        knowledgeGraphDao.tellGraph(request.graphId, request.text)
      )
      .runFold(0)((c, _) => c + 1)
      .map(IngestTextStreamResponse(_))

  override def getAnalysis(in: GetAnalysisRequest): Future[GetAnalysisResponse] =
    knowledgeGraphDao.getAnalysis(in.graphId)

  override def generateResponse(in: GenerateResponseRequest): Future[GenerateResponseResponse] =
    knowledgeGraphDao.generateResponse(in.graphId)
      .map(document => GenerateResponseResponse(in.graphId, Some(document)))

}

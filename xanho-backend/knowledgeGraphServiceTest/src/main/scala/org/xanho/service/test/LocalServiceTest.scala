package org.xanho.service.test

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.grpc.GrpcClientSettings
import akka.stream.scaladsl.Source
import org.slf4j.{Logger, LoggerFactory}
import org.xanho.proto.service.knowledgegraph._
import org.xanho.nlp.ops.TokenizerOps._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.io.StdIn

object LocalServiceTest extends App {

  implicit val system: ActorSystem[_] =
    ActorSystem(Behaviors.empty, "grpc-service-test")

  private val log: Logger =
    LoggerFactory.getLogger(this.getClass)

  private val host =
    system.settings.config.getString("service.host")

  private val port =
    system.settings.config.getInt("service.port")

  private val tlsEnabled =
    system.settings.config.getBoolean("service.tls-enabled")

  val client =
    KnowledgeGraphServiceClient(GrpcClientSettings.connectToServiceAt(host, port).withTls(tlsEnabled))

  val graphId =
    "graph2"

  while (true) {
    ???
//
//    val Some(question) =
//      client.(GenerateResponseRequest(graphId)).await.document
//
//    println(s"Xanho[$graphId]: ${question.write}")
//
//    val input = StdIn.readLine("You: ")
//
//    val ingestResult =
//      client.ingestTextStream(
//        Source(
//          List(
//            IngestTextRequest(
//              graphId = graphId,
//              text = input
//            )
//          )
//        )
//      )
//        .await
//
//    val analysis =
//      client.getAnalysis(GetAnalysisRequest(graphId = graphId)).await
//
//    log.debug("analysis={}", analysis)

  }

  system.terminate()

  implicit class FutureHelpers[T](future: Future[T]) {
    def await: T =
      future.await(20.seconds)

    def await(duration: Duration): T =
      Await.result(future, duration)
  }

}

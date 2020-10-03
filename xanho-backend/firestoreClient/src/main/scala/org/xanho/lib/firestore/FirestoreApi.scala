package org.xanho.lib.firestore

import akka.actor.typed.{ActorSystem, Extension, ExtensionId}
import akka.stream.scaladsl.{Sink, Source}
import akka.{Done, NotUsed}
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore._
import com.google.firebase.cloud.FirestoreClient
import com.google.firebase.{FirebaseApp, FirebaseOptions}

import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.util.Try

class FirestoreApi(implicit system: ActorSystem[_]) extends Extension {

  import implicits._
  import system.executionContext

  private val googleCrdentials =
    GoogleCredentials.getApplicationDefault()

  private val firebaseOptions =
    FirebaseOptions.builder()
      .setCredentials(googleCrdentials)
      .setProjectId(system.settings.config.getString("firebase.projectId"))
      .build()

  Try(FirebaseApp.initializeApp(firebaseOptions))

  implicit val firestore: Firestore = FirestoreClient.getFirestore

  def createDocumentIfNotExists(documentReference: DocumentReference): Future[Done.type] =
    documentReference
      .set(Map.empty[String, Object].asJava, SetOptions.merge())
      .scalaFuture
      .map(_ => Done)

  implicit class QueryHelpers(query: Query) {

    def unfoldSource: Source[QueryDocumentSnapshot, NotUsed] =
      unfoldSource(10)

    def unfoldSource(pageSize: Int): Source[QueryDocumentSnapshot, NotUsed] = {
      val queryWithLimit = query.limit(pageSize)

      def runQuery(q: Query): Future[Option[(Option[QuerySnapshot], QuerySnapshot)]] =
        q
          .get()
          .scalaFuture
          .map {
            case snapshot if !snapshot.isEmpty =>
              Some((Some(snapshot), snapshot))
            case _ =>
              None
          }

      Source.unfoldAsync(None: Option[QuerySnapshot]) {
        case Some(previousDoc: QuerySnapshot) =>
          runQuery(
            queryWithLimit.startAfter(previousDoc.getDocuments.asScala.last)
          )
        case _ =>
          runQuery(queryWithLimit)
      }
        .mapConcat(_.getDocuments.asScala.toList)
    }

    def deleteAll(): Future[Done] =
      query
        .unfoldSource(10)
        .map(_.getReference)
        .grouped(10)
        .map(_.foldLeft(firestore.batch())((batch, ref) => batch.delete(ref)))
        .mapAsync(10)(_.commit().scalaFuture)
        .runWith(Sink.ignore)
  }

  implicit class CollectionHelper(collection: CollectionReference) {
    def recursiveDelete(): Future[Done] =
      collection
        .unfoldSource(4)
        .mapAsync(4)(snapshot =>
          Source(
            snapshot.getReference.listCollections()
              .asScala
              .toList
          )
            .mapAsync(4)(_.recursiveDelete())
            .runWith(Sink.ignore)
            .flatMap(_ => snapshot.getReference.delete().scalaFuture)
        )
        .runWith(Sink.ignore)
  }

  implicit class ProductHelper(product: Product) {
    def toJavaMap: java.util.Map[String, Object] =
      product.getClass.getDeclaredFields
        .map(_.getName)
        .zip(
          product.productIterator
            .map {
              case p: Product => p.toJavaMap
              case a: Object => a
              case _ => null
            }
            .toList
        )
        .toMap
        .filterNot(_._2 == null)
        .asJava
  }

}

object FirestoreApi extends ExtensionId[FirestoreApi] {
  override def createExtension(system: ActorSystem[_]): FirestoreApi =
    new FirestoreApi()(system)

}
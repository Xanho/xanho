package org.xanho.lib

import akka.actor.typed.ActorSystem
import com.google.api.core.ApiFuture

import scala.concurrent.{Future, Promise}
import scala.util.Try

package object firestore {

  object implicits {

    implicit class FirestoreFutureHelper[T](apiFuture: ApiFuture[T]) {
      def scalaFuture(implicit system: ActorSystem[_]): Future[T] = {
        val promise = Promise[T]()
        apiFuture.addListener(
          () => promise.complete(Try(apiFuture.get())),
          system.executionContext
        )
        promise.future
      }
    }

  }

}

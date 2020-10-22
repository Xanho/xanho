package org.xanho.lib.firestore

import akka.Done
import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler}
import akka.stream.{Attributes, Outlet, SourceShape}
import com.google.cloud.firestore.{ListenerRegistration, Query, QuerySnapshot}

import scala.concurrent.duration._
import scala.concurrent.{Await, Promise}

class FirestoreQuerySnapshotStream(query: Query) extends GraphStage[SourceShape[QuerySnapshot]] {

  private val out: Outlet[QuerySnapshot] =
    Outlet("out")

  override val shape: SourceShape[QuerySnapshot] =
    SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = {

    new GraphStageLogic(shape) with OutHandler {
      private var listenerRegistration: ListenerRegistration = _

      private var snapshotProcessed: Promise[Done] = _

      private var buffer: QuerySnapshot = _

      override def preStart(): Unit = {
        super.preStart()
        listenerRegistration = query.addSnapshotListener(onListenerEvent)
      }

      override def postStop(): Unit = {
        super.postStop()
        listenerRegistration.remove()
      }

      override def onPull(): Unit = {
        Option(buffer).foreach { v =>
          push(out, v)
          snapshotProcessed.success(Done)
        }
      }

      private def onListenerEvent(snapshot: QuerySnapshot, exception: Throwable): Unit = {
        snapshotProcessed = Promise()

        Option(exception) match {
          case Some(exception) =>
            getAsyncCallback(handleFailure).invoke(exception)
          case _ =>
            getAsyncCallback(handleSnapshot)
              .invoke(snapshot)
            // TODO: Gross...
            Await.result(
              snapshotProcessed.future,
              1.minutes
            )
        }
      }

      private def handleSnapshot(snapshot: QuerySnapshot): Unit = {
        if (isAvailable(out)) {
          push(out, snapshot)
          snapshotProcessed.success(Done)
        }
        else {
          buffer = snapshot
        }
      }

      private def handleFailure(throwable: Throwable): Unit = {
        failStage(throwable)
        listenerRegistration.remove()
      }
    }
  }

}

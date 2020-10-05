package org.xanho.lib.firestore.persistence

import akka.actor.typed.scaladsl.adapter._
import akka.persistence.CapabilityFlag
import akka.persistence.journal.JournalSpec
import com.typesafe.config.ConfigFactory
import org.xanho.lib.firestore.FirestoreApi

import scala.concurrent.Await
import scala.concurrent.duration._

class FirestoreAsyncWriteJournalSpec extends JournalSpec(
  ConfigFactory.defaultApplication().withFallback(
    ConfigFactory.parseString(
      raw"""
        |akka.persistence.journal.plugin = "firestore-write-journal"
        |firestore-write-journal.firestore-collection = "write-journal-test-${System.currentTimeMillis()}"
        |""".stripMargin
    )
  )
) {
  override protected def supportsRejectingNonSerializableObjects: CapabilityFlag =
    false

  override protected def supportsSerialization: CapabilityFlag =
    true

  override def afterAll(): Unit = {
    val collectionName =
      config.getString("firestore-write-journal.firestore-collection")

    val firestoreApi =
      FirestoreApi(system.toTyped)

    import firestoreApi._

    Await.result(
      firestore
        .collection(collectionName)
        .recursiveDelete(),
      60.seconds
    )
    super.afterAll()
  }

}

package org.xanho.lib.firestore.persistence

import akka.actor.typed.scaladsl.adapter._
import akka.persistence.CapabilityFlag
import akka.persistence.snapshot.SnapshotStoreSpec
import com.typesafe.config.ConfigFactory
import org.xanho.lib.firestore.FirestoreApi

import scala.concurrent.Await
import scala.concurrent.duration._

class FirestoreAsyncSnapshotStoreSpec extends SnapshotStoreSpec(
  ConfigFactory.parseString(
    s"""
       |akka.persistence.snapshot-store.plugin = "firestore-snapshot-store"
       |firestore-snapshot-store.firestore-collection = "snapshot-store-test-${System.currentTimeMillis()}"
       |""".stripMargin
  ).withFallback(ConfigFactory.load())
) {

  override protected def supportsSerialization: CapabilityFlag =
    false

  override def afterAll(): Unit = {
    val collectionName =
      config.getString("firestore-snapshot-store.firestore-collection")

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

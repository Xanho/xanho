package org.xanho.lib.firestore.persistence

import akka.persistence.snapshot.SnapshotStoreSpec
import com.typesafe.config.ConfigFactory

class FirestoreAsyncSnapshotStoreSpec extends SnapshotStoreSpec(
  ConfigFactory.parseString(
    """
      |akka.persistence.snapshot-store.plugin = "firestore-snapshot-store"
      |""".stripMargin
  )
) {

}

package org.xanho.lib.firestore.persistence

import akka.persistence.CapabilityFlag
import akka.persistence.journal.JournalSpec
import com.typesafe.config.ConfigFactory

class FirestoreAsyncWriteJournalSpec extends JournalSpec(
  ConfigFactory.defaultApplication().withFallback(
    ConfigFactory.parseString(
      """
        |akka.persistence.journal.plugin = "firestore-write-journal"
        |""".stripMargin
    )
  )
) {
  override protected def supportsRejectingNonSerializableObjects: CapabilityFlag =
    false

  override protected def supportsSerialization: CapabilityFlag =
    false
}

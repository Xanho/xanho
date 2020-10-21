package org.xanho.lib.firestore

import akka.actor.typed.ActorSystem
import akka.persistence.PersistentRepr
import akka.serialization.{SerializationExtension, Serializers}
import com.google.cloud.firestore.Blob

package object persistence {
  def box(t: Any): AnyRef =
    t match {
      case v: AnyRef => v
      case v: Int => Int.box(v)
      case v: Long => Long.box(v)
      case v: Short => Short.box(v)
      case v: Byte => Byte.box(v)
      case v: Char => Char.box(v)
      case v: Float => Float.box(v)
      case v: Double => Double.box(v)
      case v: Boolean => Boolean.box(v)
      case v: Unit => Unit.box(v)
    }

  def mapToRepr(map: Map[String, AnyRef])(implicit system: ActorSystem[_]): PersistentRepr = {
    val serialization = SerializationExtension(system)
    val payload =
      serialization.deserialize(
        map("payload").asInstanceOf[Blob].toBytes,
        map("payloadSerializerId").asInstanceOf[Number].intValue(),
        map("payloadManifest").asInstanceOf[String]
      ).get
    PersistentRepr(
      payload = payload,
      sequenceNr = map("sequenceNr").asInstanceOf[java.lang.Number].longValue(),
      persistenceId = map("persistenceId").asInstanceOf[String],
      manifest = map.getOrElse("manifest", PersistentRepr.Undefined).asInstanceOf[String],
      deleted = map.get("deleted").contains(true),
      sender = null,
      writerUuid = map.getOrElse("writerUuid", PersistentRepr.Undefined).asInstanceOf[String]
    )
  }

  def persistenceReprToMap(repr: PersistentRepr)(implicit system: ActorSystem[_]): Map[String, AnyRef] = {
    val serialization = SerializationExtension(system)
    val boxedPayload =
      box(repr.payload)
    val serializer =
      serialization.findSerializerFor(boxedPayload)
    Map(
      "payload" -> Blob.fromBytes(serialization.serialize(boxedPayload).get),
      "payloadSerializerId" -> Int.box(serializer.identifier),
      "payloadManifest" -> Serializers.manifestFor(serializer, boxedPayload),
      "persistenceId" -> repr.persistenceId,
      "sequenceNr" -> Long.box(repr.sequenceNr),
      "timestamp" -> Long.box(repr.timestamp),
      "manifest" -> repr.manifest,
      "deleted" -> Boolean.box(repr.deleted),
      "writerUuid" -> repr.writerUuid,
    )
  }
}

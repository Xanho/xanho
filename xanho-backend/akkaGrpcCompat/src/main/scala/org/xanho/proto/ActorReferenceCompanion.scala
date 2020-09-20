package org.xanho.proto

import akka.actor.{ActorRef, ExtendedActorSystem}
import akka.serialization.Serialization
import scalapb.{GeneratedMessageCompanion, TypeMapper}

trait ActorReferenceCompanion {
  this: GeneratedMessageCompanion[ActorReference] =>

  private def system: ExtendedActorSystem = {
    Serialization.getCurrentTransportInformation.system.asInstanceOf[ExtendedActorSystem]
  }

  implicit val mapper: TypeMapper[ActorReference, ActorRef] = {
    TypeMapper[ActorReference, ActorRef] { v =>
      system.provider.resolveActorRef(v.serialized)
    } { v =>
      ActorReference(Serialization.serializedActorPath(v))
    }
  }
}
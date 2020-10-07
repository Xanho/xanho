import sbt._

object Dependencies {
  val AkkaVersion = "2.6.9"
  val AkkaHttpVersion = "10.2.1"

  val logging: ModuleID = "ch.qos.logback" % "logback-classic" % "1.2.3"

  def akkaModule(name: String): ModuleID =
    "com.typesafe.akka" %% s"akka-$name" % AkkaVersion

  val akkaHttp: Seq[ModuleID] =
    Seq(
      "com.typesafe.akka" %% s"akka-http" % AkkaHttpVersion,
      "com.typesafe.akka" %% s"akka-http2-support" % AkkaHttpVersion
    )

  val scalaTest: ModuleID =
    "org.scalatest" %% "scalatest" % "3.2.0" % "test"

}

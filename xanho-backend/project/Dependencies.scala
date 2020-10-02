import sbt._

object Dependencies {
  val AkkaVersion = "2.6.8"
  val AkkaHttpVersion = "10.2.0"

  val logging: ModuleID = "ch.qos.logback" % "logback-classic" % "1.2.3"

  def akkaModule(name: String): ModuleID =
    "com.typesafe.akka" %% s"akka-$name" % AkkaVersion

  val akkaHttp: ModuleID =
    "com.typesafe.akka" %% s"akka-http" % AkkaHttpVersion

  val scalaTest: ModuleID =
    "org.scalatest" %% "scalatest" % "3.2.0" % "test"

}

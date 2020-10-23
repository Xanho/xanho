import sbt._

object Dependencies {
  val AkkaVersion = "2.6.9"
  val AkkaHttpVersion = "10.2.1"

  val logging: ModuleID = "ch.qos.logback" % "logback-classic" % "1.2.3"

  def akkaModule(name: String): ModuleID =
    "com.typesafe.akka" %% s"akka-$name" % AkkaVersion

  val akkaHttp: Seq[ModuleID] =
    Seq(
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http2-support" % AkkaHttpVersion
    )

  val akkaHttpSprayJson =
    "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion

  val akkaManagementClusterHttp =
    "com.lightbend.akka.management" %% "akka-management-cluster-http" % "1.0.8"

  val akkaManagementClusterBootstrap =
    "com.lightbend.akka.management" %% "akka-management-cluster-bootstrap" % "1.0.8"

  val akkaDiscoveryKubernetesApi =
    "com.lightbend.akka.discovery" %% "akka-discovery-kubernetes-api" % "1.0.8"

  val scalaTest: ModuleID =
    "org.scalatest" %% "scalatest" % "3.2.0" % "test"

  val levelDb =
    "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8"

  val openNlp =
    "org.apache.opennlp" % "opennlp-tools" % "1.9.3"

}

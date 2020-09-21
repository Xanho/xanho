name := "xanho-backend"

version := "0.1"

scalaVersion := "2.13.3"

val commonSettings =
  Seq(
    libraryDependencies ++= Seq(
      Dependencies.scalaTest
    ),
    dependencyOverrides ++= Seq(
      Dependencies.akkaModule("discovery"),
    ),
  )

lazy val nlp =
  project

lazy val akkaGrpcCompat =
  project
    .enablePlugins(AkkaGrpcPlugin)
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.akkaModule("actor"),
        "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
      ),
    )


lazy val protos =
  project
    .enablePlugins(AkkaGrpcPlugin)
    .settings(commonSettings: _*)
    .dependsOn(akkaGrpcCompat)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.akkaModule("actor"),
        "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
      ),
    )

lazy val knowledgeGraphActor =
  project
    .settings(commonSettings: _*)
    .dependsOn(nlp, protos)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.akkaModule("actor-typed"),
        Dependencies.akkaModule("persistence-typed"),
        Dependencies.akkaModule("persistence-testkit") % Test,
      )
    )

lazy val knowledgeGraphServiceProtos =
  project
    .enablePlugins(AkkaGrpcPlugin)
    .settings(commonSettings: _*)
    .dependsOn(protos)

lazy val knowledgeGraphService =
  project
    .enablePlugins(AkkaGrpcPlugin, CloudflowApplicationPlugin, CloudflowAkkaPlugin)
    .settings(commonSettings: _*)
    .dependsOn(protos, knowledgeGraphActor, knowledgeGraphServiceProtos)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.logging,
        Dependencies.akkaModule("actor-typed"),
        Dependencies.akkaModule("stream-typed"),
        Dependencies.akkaModule("cluster-typed"),
        Dependencies.akkaModule("cluster-sharding-typed"),
        "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8"
      ),
    )

lazy val knowledgeGraphServiceTest =
  project
    .enablePlugins(AkkaGrpcPlugin)
    .settings(commonSettings: _*)
    .dependsOn(protos, knowledgeGraphActor, knowledgeGraphServiceProtos)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.logging,
      ),
    )
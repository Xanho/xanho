name := "xanho-backend"

version := "0.1"

scalaVersion := "2.13.3"

lazy val nlp =
  project

lazy val akkaGrpcCompat =
  project
    .enablePlugins(AkkaGrpcPlugin)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.akkaModule("actor"),
        "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
      ),
    )


lazy val protos =
  project
    .enablePlugins(AkkaGrpcPlugin)
    .dependsOn(akkaGrpcCompat)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.akkaModule("actor"),
        "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
      ),
    )

lazy val knowledgeGraphActor =
  project
    .dependsOn(nlp, protos)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.akkaModule("actor-typed"),
        Dependencies.akkaModule("persistence-typed"),
      )
    )

lazy val knowledgeGraphServiceProtos =
  project
    .enablePlugins(AkkaGrpcPlugin)
    .dependsOn(protos)

lazy val knowledgeGraphService =
  project
    .enablePlugins(AkkaGrpcPlugin)
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
      dependencyOverrides ++= Seq(
        Dependencies.akkaModule("discovery"),
      ),
    )

lazy val knowledgeGraphServiceTest =
  project
    .enablePlugins(AkkaGrpcPlugin)
    .dependsOn(protos, knowledgeGraphActor, knowledgeGraphServiceProtos)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.logging,
      ),
      dependencyOverrides ++= Seq(
        Dependencies.akkaModule("discovery"),
      )
    )
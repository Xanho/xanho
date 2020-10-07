name := "xanho-backend"

version := "0.1"

scalaVersion := "2.13.3"

lazy val root =
  Project(id = "root", base = file("."))
    .settings(
      name := "root",
      skip in publish := true,
    )
    .withId("root")
    .settings(commonSettings)
    .aggregate(
      nlp,
      akkaGrpcCompat,
      protos,
      knowledgeGraphActor,
      knowledgeGraphServiceProtos,
      service,
      knowledgeGraphServiceTest,
      firestoreClient,
      firestoreAkkaPersistence,
    )

val commonSettings =
  Seq(
    libraryDependencies ++= Seq(Dependencies.logging),
    libraryDependencies ++= Seq(Dependencies.scalaTest),
    dependencyOverrides ++= Seq(
      Dependencies.akkaModule("discovery"),
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

lazy val nlp =
  project
    .settings(commonSettings: _*)
    .dependsOn(protos)

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

lazy val knowledgeGraphActor =
  project
    .settings(commonSettings: _*)
    .dependsOn(nlp, protos)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.akkaModule("actor-typed"),
        Dependencies.akkaModule("persistence-typed"),
      ),
      libraryDependencies ++= Seq(
        Dependencies.akkaModule("persistence-testkit") % Test,
        "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8" % Test
      )
    )

lazy val knowledgeGraphServiceProtos =
  project
    .enablePlugins(AkkaGrpcPlugin)
    .settings(commonSettings: _*)
    .dependsOn(protos)

lazy val service =
  project
    .enablePlugins(AkkaGrpcPlugin)
    .settings(commonSettings: _*)
    .dependsOn(protos, knowledgeGraphActor, knowledgeGraphServiceProtos, firestoreAkkaPersistence)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.logging,
        Dependencies.akkaModule("actor-typed"),
        Dependencies.akkaModule("stream-typed"),
        Dependencies.akkaModule("cluster-typed"),
        Dependencies.akkaModule("cluster-sharding-typed"),
        Dependencies.akkaModule("testkit"),
        Dependencies.akkaModule("stream-testkit"),
      ),
      libraryDependencies ++= Dependencies.akkaHttp
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

lazy val firestoreClient =
  project
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        "com.google.firebase" % "firebase-admin" % "7.0.0",
        Dependencies.akkaModule("actor-typed"),
        Dependencies.akkaModule("stream-typed"),
      )
    )

lazy val firestoreAkkaPersistence =
  project
    .settings(commonSettings: _*)
    .dependsOn(firestoreClient)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.akkaModule("persistence-typed"),
        Dependencies.akkaModule("persistence-tck"),
      )
    )
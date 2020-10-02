name := "xanho-backend"

version := "0.1"

scalaVersion := "2.12.12"

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
      streamlets,
      knowledgeGraphServiceTest,
      blueprint
    )

val commonSettings =
  Seq(
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.2.3",
    ),
    libraryDependencies ++= Seq(
      Dependencies.scalaTest
    ),
    dependencyOverrides ++= Seq(
      Dependencies.akkaModule("discovery"),
    ),
  )

lazy val nlp =
  project
    .enablePlugins(CloudflowLibraryPlugin)

lazy val akkaGrpcCompat =
  project
    .enablePlugins(AkkaGrpcPlugin, CloudflowLibraryPlugin)
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.akkaModule("actor"),
        "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
      ),
    )


lazy val protos =
  project
    .enablePlugins(AkkaGrpcPlugin, CloudflowLibraryPlugin)
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
    .enablePlugins(CloudflowLibraryPlugin)
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
    .enablePlugins(AkkaGrpcPlugin, CloudflowLibraryPlugin)
    .settings(commonSettings: _*)
    .dependsOn(protos)

lazy val streamlets =
  project
    .enablePlugins(AkkaGrpcPlugin, CloudflowAkkaPlugin)
    .settings(commonSettings: _*)
    .dependsOn(protos, knowledgeGraphActor, knowledgeGraphServiceProtos)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.logging,
        Dependencies.akkaModule("actor-typed"),
        Dependencies.akkaModule("stream-typed"),
        Dependencies.akkaModule("cluster-typed"),
        Dependencies.akkaModule("cluster-sharding-typed"),
        Dependencies.akkaModule("testkit"),
        Dependencies.akkaModule("stream-testkit"),
        "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8"
      ),
    )

lazy val knowledgeGraphServiceTest =
  project
    .enablePlugins(AkkaGrpcPlugin, CloudflowLibraryPlugin)
    .settings(commonSettings: _*)
    .dependsOn(protos, knowledgeGraphActor, knowledgeGraphServiceProtos)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.logging,
      ),
    )

lazy val blueprint =
  project
    .enablePlugins(CloudflowApplicationPlugin)
    .settings(commonSettings: _*)

lazy val firestoreClient =
  project
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
//        "com.google.cloud" % "google-cloud-firestore" % "2.1.0",
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
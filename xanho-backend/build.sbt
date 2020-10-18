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
      serviceLib,
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
    .settings(
      libraryDependencies ++= Seq(
        "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
      ),
      PB.protoSources in Compile ++= Seq(
        file("../protos")
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
    .enablePlugins(AkkaGrpcPlugin)
    .settings(commonSettings: _*)
    .dependsOn(nlp, protos, akkaGrpcCompat)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.akkaModule("actor-typed"),
        Dependencies.akkaModule("persistence-typed"),
      ),
      libraryDependencies ++= Seq(
        Dependencies.akkaModule("persistence-testkit") % Test,
      )
    )

lazy val serviceLib =
  project
    .enablePlugins(AkkaGrpcPlugin)
    .settings(commonSettings: _*)
    .dependsOn(protos, knowledgeGraphActor, firestoreAkkaPersistence)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.logging,
        Dependencies.akkaModule("actor-typed"),
        Dependencies.akkaModule("stream-typed"),
        Dependencies.akkaModule("cluster-typed"),
        Dependencies.akkaModule("cluster-sharding-typed"),
        Dependencies.akkaModule("testkit"),
        Dependencies.akkaModule("stream-testkit"),
        "ch.megard" %% "akka-http-cors" % "0.4.2"
      ),
      libraryDependencies ++= Dependencies.akkaHttp
    )

lazy val serviceLocal =
  project
    .dependsOn(serviceLib)
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.levelDb,
      )
    )

lazy val serviceKubernetes =
  project
    .enablePlugins(DockerPlugin, JavaAppPackaging)
    .dependsOn(serviceLib)
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.akkaManagementClusterHttp,
        Dependencies.akkaManagementClusterBootstrap,
        Dependencies.akkaDiscoveryKubernetesApi,
      ),
      dependencyOverrides ++= Seq(
        Dependencies.akkaHttpSprayJson,
      )
    )
    .settings(
      dockerBaseImage := "adoptopenjdk:11-jre-hotspot",
      dockerExposedPorts := Seq(8080, 8558, 25520)
    )

lazy val knowledgeGraphServiceTest =
  project
    .enablePlugins(AkkaGrpcPlugin)
    .settings(commonSettings: _*)
    .dependsOn(protos, knowledgeGraphActor)
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
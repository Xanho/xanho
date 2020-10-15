package org.xanho.knowledgegraph.service

import java.nio.file.{Files, Paths}
import java.security.{KeyStore, SecureRandom}

import akka.actor.typed.{ActorSystem, Extension, ExtensionId}
import akka.http.scaladsl.{ConnectionContext, HttpsConnectionContext}
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}

class XanhoHttps(implicit system: ActorSystem[_]) extends Extension {

  private val config = system.settings.config

  private def keyPasswordLocationOpt: Option[String] =
    Some("xanho.ssl.password.path")
      .filter(config.hasPath)
      .map(config.getString)

  private def keyLocationOpt: Option[String] =
    Some("xanho.ssl.key.path")
      .filter(config.hasPath)
      .map(config.getString)

  val contextOpt: Option[HttpsConnectionContext] =
    for {
      keyPasswordLocation <- keyPasswordLocationOpt
      keyLocation <- keyLocationOpt
    } yield {
      val password =
        Files.lines(
          Paths.get(keyPasswordLocation)
        ).findFirst().get()
          .toCharArray

      val keystore = KeyStore.getInstance("JKS")
      keystore.load(
        Files.newInputStream(Paths.get(keyLocation)),
        password
      )

      val keyManagerFactory: KeyManagerFactory =
        KeyManagerFactory.getInstance("SunX509")

      keyManagerFactory.init(keystore, password)

      val trustManagerFactory = TrustManagerFactory.getInstance("SunX509")
      trustManagerFactory.init(keystore)

      val sslContext = SSLContext.getInstance("TLS")

      sslContext.init(
        keyManagerFactory.getKeyManagers,
        trustManagerFactory.getTrustManagers,
        new SecureRandom()
      )

      ConnectionContext.httpsServer(sslContext)
    }

}

object XanhoHttps extends ExtensionId[XanhoHttps] {
  override def createExtension(system: ActorSystem[_]): XanhoHttps =
    new XanhoHttps()(system)
}
package org.xanho.dictionary

import java.nio.file.{Files, Paths}

import scala.collection.JavaConverters._

case class Dictionary(words: Array[String], wordIndices: Map[String, Int]) {

  def apply(word: String): Option[Int] = wordIndices.get(word.toLowerCase)

  def apply(index: Int): Option[String] = words.lift(index)

}

object Dictionary {

  private lazy val instance: Dictionary = {
    val array: Array[String] =
      Files.lines(
        Paths.get(
          getClass.getClassLoader.getResource("words.txt").toURI
        )
      ).iterator().asScala
        .map(_.toLowerCase)
        .toArray

    val wordIndices =
      array.zipWithIndex.toMap

    Dictionary(array, wordIndices)
  }

  def apply(): Dictionary = instance

}

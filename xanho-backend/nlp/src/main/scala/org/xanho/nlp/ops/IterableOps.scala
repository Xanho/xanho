package org.xanho.nlp.ops

import scala.collection.AbstractIterator
import scala.language.implicitConversions

trait IterableOps {

  implicit def seqOps[T](items: Iterable[T]): Iterables[T] =
    new Iterables(items)

}

object IterableOps extends IterableOps

class Iterables[T](val items: Iterable[T]) extends AnyVal {

  def intersperse(value: => T): Stream[T] = {
    val iterator = items.iterator

    new AbstractIterator[T] {
      private var shouldIntersperse: Boolean = false

      override def hasNext: Boolean =
        iterator.hasNext

      override def next(): T = {
        val out =
          if (shouldIntersperse) value
          else iterator.next()
        shouldIntersperse = !shouldIntersperse
        out
      }
    }
      .toStream
  }

}
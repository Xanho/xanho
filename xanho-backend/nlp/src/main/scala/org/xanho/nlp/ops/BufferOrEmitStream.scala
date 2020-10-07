package org.xanho.nlp.ops

import scala.collection.AbstractIterator
import scala.util.Try

object BufferOrEmitStream {

  import scala.collection.mutable

  sealed abstract class BufferOrEmit[I, O]

  case class Buffer[I, O](i: I) extends BufferOrEmit[I, O]

  case class Emit[I, O](elements: List[O]) extends BufferOrEmit[I, O]

  def apply[I, O](
                   inputs: Iterable[I],
                   f: (I, Seq[I]) => BufferOrEmit[I, O],
                   flushBuffer: Seq[I] => Seq[O]
                 ): Stream[O] = {
    val iterator = inputs.toIterator

    new AbstractIterator[O] {

      private val elementBuffer = mutable.Buffer.empty[I]
      private val outBuffer = mutable.Queue.empty[O]

      override def hasNext: Boolean =
        elementBuffer.nonEmpty || iterator.hasNext || outBuffer.nonEmpty

      override def next(): O =
        if (outBuffer.nonEmpty) {
          outBuffer.dequeue()
        } else if (!iterator.hasNext) {
          val remaining = elementBuffer.toList
          elementBuffer.clear()
          flushBuffer(remaining).toList match {
            case out :: rest =>
              outBuffer ++= rest
              out
          }
        } else {
          var emit: Emit[I, O] = null
          while (emit == null && iterator.hasNext) {
            val t = Try(iterator.next())
            val next = t.get
            f(next, elementBuffer) match {
              case e: Emit[I, O] if e.elements.nonEmpty => emit = e
              case _: Emit[_, _] =>
              case Buffer(element: I@unchecked) => elementBuffer.append(element)
            }
          }

          val emitOrFinalEmit =
            Option(emit).getOrElse {
              val remaining = elementBuffer.toList
              Emit(flushBuffer(remaining).toList)
            }

          elementBuffer.clear()

          emitOrFinalEmit match {
            case Emit(token :: otherTokens) =>
              outBuffer.enqueue(otherTokens: _*)
              token
          }
        }
    }
      .toStream
  }
}

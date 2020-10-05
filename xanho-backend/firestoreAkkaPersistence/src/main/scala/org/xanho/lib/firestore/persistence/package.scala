package org.xanho.lib.firestore

package object persistence {
  def box(t: Any): AnyRef =
    t match {
      case v: AnyRef => v
      case v: Int => Int.box(v)
      case v: Long => Long.box(v)
      case v: Short => Short.box(v)
      case v: Byte => Byte.box(v)
      case v: Char => Char.box(v)
      case v: Float => Float.box(v)
      case v: Double => Double.box(v)
      case v: Boolean => Boolean.box(v)
      case v: Unit => Unit.box(v)
    }
}

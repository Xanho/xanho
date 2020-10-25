package org.xanho.graph.ops

import org.xanho.proto.graph._

import scala.language.implicitConversions

trait EdgeOps {
  implicit def edgeOps(edge: Edge): EdgeExt =
    new EdgeExt(edge)
}

object EdgeOps extends EdgeOps

class EdgeExt(val edge: Edge) extends AnyVal {
  def source(implicit graph: Graph): Node = graph.nodes(edge.sourceId)

  def destination(implicit graph: Graph): Node = graph.nodes(edge.destinationId)

  def dataOrEmpty: DataObject =
    edge.data.getOrElse(DataObject.defaultInstance)

}

package org.xanho.graph.ops

import org.xanho.proto.graph._

import scala.language.implicitConversions

trait NodeOps {
  implicit def nodeOps(node: Node): NodeExt =
    new NodeExt(node)
}

object NodeOps extends NodeOps

class NodeExt(val node: Node) extends AnyVal {
  def edges(implicit graph: Graph): Stream[Edge] =
    sourceEdges ++ destinationEdges

  def sourceEdges(implicit graph: Graph): Stream[Edge] =
    graph.edges.toStream.filter(_.sourceId == node.id)

  def destinationEdges(implicit graph: Graph): Stream[Edge] =
    graph.edges.toStream.filter(_.destinationId == node.id)

  def dataOrEmpty: DataObject =
    node.data.getOrElse(DataObject.defaultInstance)
}

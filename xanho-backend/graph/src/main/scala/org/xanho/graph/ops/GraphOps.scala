package org.xanho.graph.ops

import org.xanho.proto.graph._

import scala.language.implicitConversions

trait GraphOps {
  implicit def graphOps(graph: Graph): GraphExt =
    new GraphExt(graph)
}

object GraphOps extends GraphOps

class GraphExt(val graph: Graph) extends AnyVal {
  def nextNodeId: Int = graph.nodes.length

  def nextEdgeId: Int = graph.edges.length

  def withNode(nodeType: String, data: DataObject): (Graph, Node) = {
    val node =
      Node()
        .withId(nextNodeId)
        .withNodeType(nodeType)
        .withData(data)

    (graph.addNodes(node), node)
  }

  def withEdge(edgeType: String, source: Node, destination: Node, data: DataObject): (Graph, Edge) = {
    val edge =
      Edge()
        .withId(nextEdgeId)
        .withEdgeType(edgeType)
        .withSourceId(source.id)
        .withDestinationId(destination.id)
        .withData(data)

    (graph.addEdges(edge), edge)
  }

  def nodesByType(nodeType: String): Stream[Node] =
    graph.nodes.toStream.filter(_.nodeType == nodeType)

  def edgesByType(edgeType: String): Stream[Edge] =
    graph.edges.toStream.filter(_.edgeType == edgeType)
}

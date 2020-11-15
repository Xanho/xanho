package org.xanho.graph.ops

import org.xanho.proto.graph._

import scala.language.implicitConversions

import org.xanho.graph.ops.implicits._

trait GraphOps {
  implicit def graphOps(graph: Graph): GraphExt =
    new GraphExt(graph)
}

object GraphOps extends GraphOps

class GraphExt(val graph: Graph) extends AnyVal {
  def nextNodeId: Int = graph.nodes.length

  def nextEdgeId: Int = graph.edges.length

  def node(id: Int): Option[Node] =
    graph.nodes.find(_.id == id)

  def edge(id: Int): Option[Edge] =
    graph.edges.find(_.id == id)

  def withNode(nodeType: String, data: DataObject): (Graph, Node) = {
    val node =
      Node()
        .withId(nextNodeId)
        .withNodeType(nodeType)
        .withData(data)

    (graph.addNodes(node), node)
  }
  
  def withNode(node: Node): (Graph, Node) =
    graph.nodes.indexWhere(_.id == node.id) match {
      case -1 =>
        (graph.addNodes(node), node)
      case i =>
        (graph.withNodes(graph.nodes.updated(i, node)), node)
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
  
  def withEdge(edge: Edge): (Graph, Edge) =
    graph.edges.indexWhere(_.id == edge.id) match {
      case -1 =>
        (graph.addEdges(edge), edge)
      case i =>
        (graph.withEdges(graph.edges.updated(i, edge)), edge)
    }

  def withUpdatedNode(id: Int, updatedData: DataObject): Option[(Graph, Node)] = {
    val updatedGraph = graph.withNodes(graph.nodes.map {
      case node if node.id == id => node.copy(data = Some(updatedData))
      case node => node
    })

    updatedGraph.node(id).map((updatedGraph, _))
  }

  def withUpdatedEdge(id: Int, updatedData: DataObject): Option[(Graph, Edge)] = {
    val updatedGraph = graph.withEdges(graph.edges.map {
      case edge if edge.id == id => edge.copy(data = Some(updatedData))
      case edge => edge
    })

    updatedGraph.edge(id).map((updatedGraph, _))
  }

  def nodesByType(nodeType: String): Stream[Node] =
    graph.nodes.toStream.filter(_.nodeType == nodeType)

  def edgesByType(edgeType: String): Stream[Edge] =
    graph.edges.toStream.filter(_.edgeType == edgeType)
}

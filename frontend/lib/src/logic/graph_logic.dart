import 'package:flutter/material.dart';
import 'package:frontend/src/proto/org/xanho/proto/graph/graph.pb.dart';

extension GraphOps on Graph {
  Node node(int id) => nodes.firstWhere((node) => node.id == id);
  Edge edge(int id) => edges.firstWhere((edge) => edge.id == id);

  Iterable<Node> nodesOfType(String nodeType) =>
      nodes.where((node) => node.nodeType == nodeType);

  Iterable<Edge> edgesOfType(String edgeType) =>
      edges.where((edge) => edge.edgeType == edgeType);

  void addNodeIfNotExists(Node node) {
    if (!nodes.contains(node)) nodes.add(node);
  }

  void addEdgeIfNotExists(Edge edge) {
    if (!edges.contains(edge)) edges.add(edge);
  }

  Graph expandAround(Node node, Graph parentGraph) {
    final newGraph = clone();

    node.sourceEdges(parentGraph).forEach(
      (edge) {
        newGraph.addNodeIfNotExists(edge.destination(parentGraph));
        newGraph.addEdgeIfNotExists(edge);
      },
    );
    node.destinationEdges(parentGraph).forEach(
      (edge) {
        newGraph.addNodeIfNotExists(edge.source(parentGraph));
        newGraph.addEdgeIfNotExists(edge);
      },
    );
    return newGraph;
  }
}

extension NodeOps on Node {
  Iterable<Edge> sourceEdges(Graph graph) =>
      graph.edges.where((edge) => edge.sourceId == this.id);

  Iterable<Edge> destinationEdges(Graph graph) =>
      graph.edges.where((edge) => edge.destinationId == this.id);
}

extension EdgeOps on Edge {
  Node source(Graph graph) =>
      graph.nodes.firstWhere((node) => node.id == this.sourceId);
  Node destination(Graph graph) =>
      graph.nodes.firstWhere((node) => node.id == this.destinationId);
}

class GraphDeltas {
  GraphDeltas(
      {@required this.newNodes,
      @required this.deletedNodes,
      @required this.newEdges,
      @required this.deletedEdges});
  Set<Node> newNodes;
  Set<Node> deletedNodes;
  Set<Edge> newEdges;
  Set<Edge> deletedEdges;

  GraphDeltas.fromGraphs(Graph oldGraph, Graph newGraph)
      : this(
          newNodes: Set.of(
            newGraph.nodes
                .where((newNode) => !oldGraph.nodes.contains(newNode)),
          ),
          deletedNodes: Set.of(
            oldGraph.nodes
                .where((oldNode) => !newGraph.nodes.contains(oldNode)),
          ),
          newEdges: Set.of(
            newGraph.edges
                .where((newEdge) => !oldGraph.edges.contains(newEdge)),
          ),
          deletedEdges: Set.of(
            oldGraph.edges
                .where((oldEdge) => !newGraph.edges.contains(oldEdge)),
          ),
        );
}

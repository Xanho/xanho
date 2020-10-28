import 'package:frontend/src/proto/org/xanho/proto/graph/graph.pb.dart';

Graph subGraphAround(Node node, Graph graph) {
  var maxNodes = 30;
  var idx = 0;

  final newGraph = Graph();
  newGraph.nodes.add(node);

  while (newGraph.nodes.length < maxNodes && idx < newGraph.nodes.length) {
    var currentNode = newGraph.nodes[idx];
    currentNode.sourceEdges(graph).forEach(
      (edge) {
        if (!newGraph.edges.contains(edge)) {
          newGraph.edges.add(edge);
        }
        final destinationNode = graph.nodes[edge.destinationId];
        if (!newGraph.nodes.contains(destinationNode)) {
          newGraph.nodes.add(destinationNode);
        }
      },
    );
    currentNode.destinationEdges(graph).forEach(
      (edge) {
        if (!newGraph.edges.contains(edge)) {
          newGraph.edges.add(edge);
        }
        final sourceNode = graph.nodes[edge.sourceId];
        if (!newGraph.nodes.contains(sourceNode)) {
          newGraph.nodes.add(sourceNode);
        }
      },
    );
    idx++;
  }

  newGraph.nodes.sort((nodeA, nodeB) => nodeA.id.compareTo(nodeB.id));
  newGraph.edges.sort((edgeA, edgeB) => edgeA.id.compareTo(edgeB.id));

  return newGraph;
}

extension GraphOps on Graph {}

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

import 'package:frontend/src/proto/org/xanho/proto/graph/graph.pb.dart';
import 'package:frontend/src/logic/graph_logic.dart';

extension NlpGraphOps on Graph {
  Graph get wordGraph => Graph()
    ..nodes.addAll(nodesOfType("word"))
    ..edges.addAll(edgesOfType("wordWord")
        .where((edge) => edge.data.values["association"].doubleValue > 0.1));
}

extension NlpNodeOps on Node {
  double relavence(Graph graph) {
    final allEdges = List<Edge>();
    allEdges.addAll(sourceEdges(graph));
    allEdges.addAll(destinationEdges(graph));

    final associations = allEdges
        .where((edge) => edge.edgeType == "wordWord")
        .map((edge) => edge.data.values["association"].doubleValue);

    final summedAssociations =
        associations.fold(0, (res, value) => res + value);

    return summedAssociations / graph.nodes.length;
  }
}

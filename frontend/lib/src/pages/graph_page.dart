import 'dart:async';

import 'package:flutter/material.dart';
import 'package:frontend/src/logic/graph_service.dart';
import 'package:frontend/src/pages/loading_page.dart';
import 'package:frontend/src/proto/org/xanho/proto/graph/graph.pb.dart'
    as graphPb;
import 'package:graphview/GraphView.dart';
import 'package:provider/provider.dart';
import 'package:frontend/src/logic/graph_logic.dart';
import 'package:frontend/src/logic/nlp_graph_logic.dart';
import 'dart:math';

class GraphPage extends StatefulWidget {
  const GraphPage(this.graphId);

  final String graphId;

  @override
  State<StatefulWidget> createState() => _GraphPageState();
}

class _GraphPageState extends State<GraphPage> {
  @override
  Widget build(BuildContext context) {
    return FutureBuilder<graphPb.Graph>(
      future: context.watch<GraphService>().getGraph(widget.graphId),
      builder: (context, snapshot) =>
          snapshot.hasData ? _graphPage(context, snapshot.data) : LoadingPage(),
    );
  }

  _graphPage(BuildContext context, graphPb.Graph graph) => Scaffold(
        appBar: AppBar(title: Text("Graph View")),
        body: GraphViewParent(graph: graph.wordGraph),
      );
}

class GraphViewParent extends StatefulWidget {
  GraphViewParent({@required this.graph});
  final graphPb.Graph graph;

  @override
  State<StatefulWidget> createState() => _GraphViewParentState();
}

class _GraphViewParentState extends State<GraphViewParent> {
  graphPb.Graph _currentPartialGraph;
  StreamController<graphPb.Graph> _streamController;
  graphPb.Node _focusedNode;

  @override
  void initState() {
    super.initState();

    this._currentPartialGraph = widget.graph;

    this._focusedNode = this
        ._currentPartialGraph
        .nodes
        .firstWhere((node) => node.nodeType == "word");

    _streamController = StreamController<graphPb.Graph>();

    _streamController.add(this._currentPartialGraph);
  }

  static graphPb.Graph _initialPartialGraph(graphPb.Graph rootGraph) {
    graphPb.Graph graph = graphPb.Graph();
    rootGraph.nodes
        .where((node) => node.nodeType == "document")
        .forEach((node) {
      graph.addNodeIfNotExists(node);
      graph.expandAround(node, rootGraph);
    });
    return graph;
  }

  @override
  Widget build(BuildContext context) {
    return GraphViewPage(
      key: UniqueKey(),
      initialGraph: _currentPartialGraph,
      graphChanges: this._streamController.stream,
      focusedNode: _focusedNode,
      onNodeTapped: this._onNodeTapped,
    );
  }

  _onNodeTapped(graphPb.Node node) {
    final newGraph = _currentPartialGraph.expandAround(node, widget.graph);
    _currentPartialGraph = newGraph;
    _streamController.add(newGraph);
  }
}

class GraphViewPage extends StatefulWidget {
  GraphViewPage(
      {Key key,
      @required this.initialGraph,
      @required this.graphChanges,
      @required this.focusedNode,
      @required this.onNodeTapped})
      : super(key: key);

  final graphPb.Graph initialGraph;
  final Stream<graphPb.Graph> graphChanges;
  final graphPb.Node focusedNode;
  final Function(graphPb.Node) onNodeTapped;

  @override
  _GraphViewPageState createState() => _GraphViewPageState();
}

class _GraphViewPageState extends State<GraphViewPage> {
  final Graph graphView = Graph();

  final Layout fruchterman = FruchtermanReingoldAlgorithm(iterations: 100);

  graphPb.Graph previousGraph = graphPb.Graph();

  Map<graphPb.Node, Node> _nodeMapping = Map();
  Map<graphPb.Edge, Edge> _edgeMapping = Map();

  StreamSubscription<graphPb.Graph> _subscription;

  @override
  void initState() {
    super.initState();
    _updateGraph(widget.initialGraph);
    this._subscription = widget.graphChanges.listen(_updateGraph);
    previousGraph.addNodeIfNotExists(widget.focusedNode);
    Node nodeView = _nodeView(widget.focusedNode);
    _nodeMapping[widget.focusedNode] = nodeView;
    graphView.addNode(nodeView);
  }

  @override
  void dispose() {
    super.dispose();
    _subscription.cancel();
  }

  void _updateGraph(graphPb.Graph newGraph) {
    final deltas = GraphDeltas.fromGraphs(previousGraph, newGraph);
    previousGraph = newGraph;
    graphView.removeEdges(
        deltas.deletedEdges.map((edge) => _edgeMapping[edge]).toList());
    graphView.removeNodes(
        deltas.deletedNodes.map((node) => _nodeMapping[node]).toList());
    deltas.newNodes.forEach((node) {
      Node nodeView = _nodeView(node);
      _nodeMapping[node] = nodeView;
      graphView.addNode(nodeView);
    });
    deltas.newEdges.forEach((edge) {
      final sourceNodeView = _nodeMapping[edge.source(newGraph)];
      final destinationNodeView = _nodeMapping[edge.destination(newGraph)];
      final edgeView = Edge(
        sourceNodeView,
        destinationNodeView,
        paint: Paint()
          ..color = edge.color(newGraph)
          ..strokeWidth = 2
          ..style = PaintingStyle.fill,
      );
      _edgeMapping[edge] = edgeView;
      graphView.addEdgeS(edgeView);
    });
    setState(() {});
  }

  Node _nodeView(graphPb.Node node) {
    Node nodeView;
    nodeView = Node(
      NodeWidget(
        node: node,
        graph: previousGraph,
        onTap: () {
          fruchterman.setFocusedNode(nodeView);
          widget.onNodeTapped(node);
        },
      ),
    );
    return nodeView;
  }

  @override
  Widget build(BuildContext context) {
    var graphView2 = GraphView(
      graph: graphView,
      algorithm: fruchterman,
      paint: Paint()
        ..color = Colors.white
        ..strokeWidth = 1
        ..style = PaintingStyle.fill,
    );

    final interactive = InteractiveViewer(
      child: graphView2,
      constrained: false,
      maxScale: 4,
      minScale: 0.1,
    );
    return interactive;
  }
}

class NodeWidget extends StatelessWidget {
  NodeWidget({@required this.node, @required this.graph, @required this.onTap});

  final graphPb.Node node;
  final graphPb.Graph graph;
  final Function() onTap;

  @override
  Widget build(BuildContext context) {
    var container = Container(
      padding: EdgeInsets.all(12),
      child: Text(node.text),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(4),
        color: node.boxColor(graph),
      ),
    );

    final gestureDetector = GestureDetector(
      onTap: onTap,
      child: container,
    );
    return gestureDetector;
  }
}

extension NodeUiOps on graphPb.Node {
  String get text {
    switch (nodeType) {
      case "word":
        return data.values["value"].stringValue;
        break;
      case "punctuation":
        return data.values["value"].stringValue;
        break;
      case "sentence":
        return "Sentence";
        break;
      case "paragraph":
        return "Paragraph";
        break;
      case "document":
        return "Document";
        break;
      case "phrase":
        return "Phrase";
        break;
    }

    return nodeType;
  }

  double _wordAlpha(graphPb.Graph graph) => min(relavence(graph), 1);

  Color boxColor(graphPb.Graph graph) {
    switch (nodeType) {
      case "word":
        final alpha = _wordAlpha(graph);
        return Colors.black
            .withBlue(50)
            .withRed(50)
            .withGreen((50 + alpha * 205).round());
        break;
      case "punctuation":
        return Colors.green[800];
        break;
      case "sentence":
        return Colors.purple;
        break;
      case "paragraph":
        return Colors.blue;
        break;
      case "document":
        return Colors.red;
        break;
      case "phrase":
        return Colors.deepOrange;
        break;
    }

    return Colors.blue;
  }
}

extension EdgeUiOps on graphPb.Edge {
  Color color(graphPb.Graph graph) {
    switch (edgeType) {
      case "wordWord":
        final association = data.values["association"]?.doubleValue;
        final double alpha = (association != null)
            ? max(
                min(
                    (1.toDouble() -
                        1.toDouble() /
                            (association * log(graph.edges.length.toDouble()))),
                    1),
                0)
            : 0;
        return Colors.black
            .withBlue(50)
            .withRed(50)
            .withGreen((50 + alpha * 205).round());
      case "phraseWord":
        return Colors.green[200];
      case "sentencePunctuation":
        return Colors.lime[200];
      case "sentencePhrase":
        return Colors.orange[200];
      case "paragraphSentence":
        return Colors.purple[200];
      case "documentParagraph":
        return Colors.blue[200];
    }
    return Colors.white;
  }
}

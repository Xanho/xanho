import 'dart:math';

import 'package:flutter/material.dart';
import 'package:frontend/src/logic/graph_service.dart';
import 'package:frontend/src/pages/loading_page.dart';
import 'package:frontend/src/proto/org/xanho/proto/graph/graph.pb.dart'
    as graphPb;
import 'package:graphview/GraphView.dart';
import 'package:provider/provider.dart';
import 'package:frontend/src/logic/graph_logic.dart' as graphLogic;

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
        body: GraphViewParent(graph: graph),
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

  @override
  void initState() {
    super.initState();
    var randomFocus =
        widget.graph.nodes[Random().nextInt(widget.graph.nodes.length)];
    this._currentPartialGraph = graphLogic.subGraphAround(
      randomFocus,
      widget.graph,
    );
  }

  @override
  Widget build(BuildContext context) {
    return GraphViewPage(
      key: UniqueKey(),
      graph: this._currentPartialGraph,
      onNodeTapped: this._onNodeTapped,
    );
  }

  _onNodeTapped(graphPb.Node node) {
    final newGraph = graphLogic.subGraphAround(node, widget.graph);
    setState(() {
      this._currentPartialGraph = newGraph;
    });
  }
}

class GraphViewPage extends StatefulWidget {
  GraphViewPage({Key key, @required this.graph, @required this.onNodeTapped})
      : super(key: key);

  final graphPb.Graph graph;
  final Function(graphPb.Node) onNodeTapped;

  @override
  _GraphViewPageState createState() => _GraphViewPageState();
}

class _GraphViewPageState extends State<GraphViewPage> {
  final Graph graphView = Graph();

  final Layout fruchterman = FruchtermanReingoldAlgorithm(iterations: 100);

  List<Node> _nodes = List<Node>();

  @override
  void initState() {
    super.initState();
    _nodes = widget.graph.nodes
        .map((node) => Node(
              NodeWidget(
                node: node,
                onTap: () => widget.onNodeTapped(node),
              ),
            ))
        .toList();
    graphView.addNodes(_nodes);
    widget.graph.edges.forEach((edge) => graphView.addEdge(
          _nodes.elementAt(widget.graph.nodes
              .indexWhere((node) => node.id == edge.sourceId)),
          _nodes.elementAt(widget.graph.nodes
              .indexWhere((node) => node.id == edge.destinationId)),
        ));
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

    return SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      child: SingleChildScrollView(
        child: graphView2,
      ),
    );
  }
}

class NodeWidget extends StatelessWidget {
  NodeWidget({@required this.node, @required this.onTap});

  final graphPb.Node node;
  final Function() onTap;

  @override
  Widget build(BuildContext context) {
    var container = Container(
      padding: EdgeInsets.all(12),
      child: Text(_text),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(4),
        // boxShadow: [BoxShadow(color: Colors.blue[100], spreadRadius: 1)],
        color: _boxColor,
      ),
    );

    final gestureDetector = GestureDetector(
      onTap: onTap,
      child: container,
    );
    return gestureDetector;
  }

  get _text {
    switch (node.nodeType) {
      case "word":
        return node.data.values["value"].stringValue;
        break;
      case "punctuation":
        return node.data.values["value"].stringValue;
        break;
      case "sentence":
        return "S";
        break;
      case "paragraph":
        return "P";
        break;
      case "document":
        return "D";
        break;
      case "phrase":
        return "P";
        break;
    }

    return node.nodeType;
  }

  get _boxColor {
    switch (node.nodeType) {
      case "word":
        return Colors.green;
        break;
      case "punctuation":
        return Colors.yellow[800];
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
  }
}

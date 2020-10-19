import 'dart:async';

import 'package:flutter/material.dart';

import 'package:frontend/src/logic/graph_service.dart';
import 'package:frontend/src/widgets/chat.dart';
import 'package:provider/provider.dart';

class GraphPage extends StatefulWidget {
  const GraphPage(this._graphId);

  final String _graphId;

  @override
  State<StatefulWidget> createState() {
    return _GraphPageState();
  }
}

class _GraphPageState extends State<GraphPage> {
  @override
  Widget build(BuildContext context) {
    return Provider<GraphService>(
      create: (_) => GraphService(),
      builder: (context, _) => GraphPageWithService(
        context.watch<GraphService>(),
        widget._graphId,
      ),
    );
  }
}

class GraphPageWithService extends StatefulWidget {
  GraphPageWithService(this._graphService, this._graphId);

  final GraphService _graphService;
  final String _graphId;

  @override
  State<StatefulWidget> createState() {
    return _GraphPageWithServiceState();
  }
}

class _GraphPageWithServiceState extends State<GraphPageWithService> {
  StreamController<Message> _streamController;
  Function(Message) _sendMessage;

  @override
  void initState() {
    super.initState();
    _streamController = StreamController<Message>();
    _sendMessage = (message) async {
      await widget._graphService.sendMessage(widget._graphId, message.text);
      _streamController.add(message);
      final nextMessage = await widget._graphService
          .receiveMessage(widget._graphId)
          .then((m) => Message(m, MessageSide.left));
      _streamController.add(nextMessage);
    };
  }

  @override
  Widget build(BuildContext context) {
    return GraphPageImpl(
      messagesStream: _streamController.stream,
      sendMessage: this._sendMessage,
    );
  }
}

class GraphPageImpl extends StatefulWidget {
  GraphPageImpl({this.messagesStream, this.sendMessage});

  final Stream<Message> messagesStream;
  final Function(Message) sendMessage;

  @override
  _GraphPageImplState createState() => _GraphPageImplState();
}

class _GraphPageImplState extends State<GraphPageImpl> {
  final _scrollController = ScrollController();

  final _formKey = GlobalKey<FormState>();

  final _textFieldController = TextEditingController();

  Stream<List<Message>> get _statefulStream {
    var items = new List<Message>();
    return widget.messagesStream.map((item) {
      items = List.of(items);
      items.add(item);
      return items;
    });
  }

  _transitionToBottom() {
    _scrollController.animateTo(
      _scrollController.position.maxScrollExtent,
      duration: Duration(milliseconds: 500),
      curve: Curves.fastOutSlowIn,
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Xanho Graph"),
      ),
      body: _body(context),
    );
  }

  Widget _streamBuilder(BuildContext context) {
    final onItems = (List<Message> items) {
      Timer(
        Duration(milliseconds: 200),
        () {
          _transitionToBottom();
        },
      );
      return items;
    };

    return StreamBuilder<List<Message>>(
      stream: _statefulStream.map(onItems),
      initialData: [],
      builder: (context, snapshot) => _listView(snapshot.data ?? []),
    );
  }

  _form(BuildContext context) {
    return Form(
        key: _formKey,
        child: Column(children: <Widget>[
          TextFormField(
            validator: (value) {
              if (value.isEmpty) {
                return "Please enter a Graph ID.";
              }
              return null;
            },
            controller: _textFieldController,
          ),
          IconButton(
            onPressed: () {
              if (_formKey.currentState.validate()) {
                _onValidInput();
              }
            },
            icon: Icon(Icons.navigate_next_rounded),
          )
        ]));
  }

  _onValidInput() {
    widget.sendMessage(Message(_textFieldController.text, MessageSide.right));
    _textFieldController.clear();
  }

  _body(BuildContext context) {
    return Center(
      child: ConstrainedBox(
        constraints: BoxConstraints(maxWidth: 1080),
        child: Column(
          children: [
            Flexible(child: _streamBuilder(context)),
            _form(context),
          ],
        ),
      ),
    );
  }

  _listView(List<Message> messages) => ListView.builder(
        itemCount: messages.length,
        itemBuilder: (context, index) =>
            ChatBubble.fromMessage(messages[index]),
        controller: _scrollController,
      );
}

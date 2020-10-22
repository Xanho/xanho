import 'dart:async';

import 'package:fixnum/fixnum.dart';
import 'package:flutter/material.dart';

import 'package:frontend/src/logic/graph_service.dart';
import 'package:frontend/src/widgets/chat.dart';
import 'package:provider/provider.dart';
import 'package:uuid/uuid.dart';

import '../proto/org/xanho/proto/service/knowledgeGraph.pb.dart';
import '../widgets/chat.dart';

class ChatPage extends StatefulWidget {
  const ChatPage(this._graphId);

  final String _graphId;

  @override
  State<StatefulWidget> createState() {
    return _ChatPageState();
  }
}

class _ChatPageState extends State<ChatPage> {
  @override
  Widget build(BuildContext context) {
    return Provider<GraphService>(
      create: (_) => GraphService(),
      builder: (context, _) => ChatPageWithService(
        context.watch<GraphService>(),
        widget._graphId,
      ),
    );
  }
}

class ChatPageWithService extends StatefulWidget {
  ChatPageWithService(this._graphService, this._graphId);

  final GraphService _graphService;
  final String _graphId;

  @override
  State<StatefulWidget> createState() {
    return _ChatPageWithServiceState();
  }
}

class _ChatPageWithServiceState extends State<ChatPageWithService> {
  StreamController<ChatBubbleMessage> _streamController;
  Function(ChatBubbleMessage) _sendMessage;

  @override
  void initState() {
    super.initState();
    _streamController = StreamController<ChatBubbleMessage>();
    _sendMessage = (message) {
      final textMessage = TextMessage()
        ..id = Uuid().v4()
        ..source = MessageSource.USER
        ..text = message.text
        ..timestampMs = Int64(DateTime.now().millisecondsSinceEpoch);

      widget._graphService.sendMessage(widget._graphId, textMessage);
      _streamController.add(message);
    };
  }

  @override
  Widget build(BuildContext context) {
    return ChatPageImpl(
      messagesStream: widget._graphService.messagesStream(widget._graphId).map(
            (textMessage) => ChatBubbleMessage(
                textMessage.text,
                textMessage.source == MessageSource.SYSTEM
                    ? ChatBubbleMessageSide.left
                    : ChatBubbleMessageSide.right),
          ),
      sendMessage: this._sendMessage,
    );
  }
}

class ChatPageImpl extends StatefulWidget {
  ChatPageImpl({this.messagesStream, this.sendMessage});

  final Stream<ChatBubbleMessage> messagesStream;
  final Function(ChatBubbleMessage) sendMessage;

  @override
  _ChatPageImplState createState() => _ChatPageImplState();
}

class _ChatPageImplState extends State<ChatPageImpl> {
  final _scrollController = ScrollController();

  final _formKey = GlobalKey<FormState>();

  final _textFieldController = TextEditingController();

  Stream<List<ChatBubbleMessage>> get _statefulStream {
    var items = new List<ChatBubbleMessage>();
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
    final onItems = (List<ChatBubbleMessage> items) {
      Timer(
        Duration(milliseconds: 200),
        () {
          _transitionToBottom();
        },
      );
      return items;
    };

    return StreamBuilder<List<ChatBubbleMessage>>(
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
    widget.sendMessage(ChatBubbleMessage(
        _textFieldController.text, ChatBubbleMessageSide.right));
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

  _listView(List<ChatBubbleMessage> messages) => ListView.builder(
        itemCount: messages.length,
        itemBuilder: (context, index) =>
            ChatBubble.fromMessage(messages[index]),
        controller: _scrollController,
      );
}

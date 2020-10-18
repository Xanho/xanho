import 'dart:async';

import 'package:flutter/material.dart';

import '../logic/graph_service.dart';
import 'package:provider/provider.dart';

class GraphPage extends StatefulWidget {
  const GraphPage(this._graphId);

  final String _graphId;

  @override
  State<StatefulWidget> createState() {
    return _GraphPageState(this._graphId);
  }
}

class _GraphPageState extends State<GraphPage> {
  _GraphPageState(this._graphId);

  final String _graphId;

  @override
  Widget build(BuildContext context) {
    return Provider<GraphService>(
      create: (_) => GraphService(),
      builder: (context, _) => _GraphPageWithService(
        context.watch<GraphService>(),
        _graphId,
      ),
    );
  }
}

class _GraphPageWithService extends StatefulWidget {
  _GraphPageWithService(this._graphService, this._graphId);

  final GraphService _graphService;
  final String _graphId;

  @override
  State<StatefulWidget> createState() {
    return _GraphPageWithServiceState(this._graphService, this._graphId);
  }
}

class _GraphPageWithServiceState extends State<_GraphPageWithService> {
  _GraphPageWithServiceState(this._graphService, this._graphId);

  final GraphService _graphService;
  final String _graphId;

  StreamController<_Message> _streamController;
  Function(_Message) _sendMessage;

  @override
  void initState() {
    super.initState();
    _streamController = StreamController<_Message>();
    _sendMessage = (message) async {
      await _graphService.sendMessage(_graphId, message.text);
      _streamController.add(message);
      final nextMessage = await _graphService
          .receiveMessage(_graphId)
          .then((m) => _Message(m, _MessageSide.left));
      _streamController.add(nextMessage);
    };
  }

  @override
  Widget build(BuildContext context) {
    return _GraphPageImpl(
      messagesStream: _streamController.stream,
      sendMessage: this._sendMessage,
    );
  }
}

class _GraphPageImpl extends StatelessWidget {
  _GraphPageImpl({this.messagesStream, this.sendMessage});

  Stream<_Message> messagesStream;
  Function(_Message) sendMessage;

  final _scrollController = ScrollController();

  final _formKey = GlobalKey<FormState>();

  final _textFieldController = TextEditingController();

  Stream<List<_Message>> get _statefulStream {
    var items = new List<_Message>();
    return messagesStream.map((item) {
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
    final streamBuilder = StreamBuilder<List<_Message>>(
      stream: _statefulStream.map((items) {
        Timer(
          Duration(milliseconds: 200),
          () {
            _transitionToBottom();
          },
        );
        return items;
      }),
      initialData: [],
      builder: (context, snapshot) => _listView(snapshot.data ?? []),
    );

    final form = Form(
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
                sendMessage(
                    _Message(_textFieldController.text, _MessageSide.right));
                _textFieldController.clear();
              }
            },
            icon: Icon(Icons.navigate_next_rounded),
          )
        ]));

    return Scaffold(
      appBar: AppBar(
        title: Text("Xanho Graph"),
      ),
      body: Center(
        child: ConstrainedBox(
          constraints: BoxConstraints(maxWidth: 1080),
          child: Column(
            children: [
              Flexible(
                fit: FlexFit.loose,
                child: streamBuilder,
              ),
              form,
            ],
          ),
        ),
      ),
    );
  }

  _listView(List<_Message> messages) => ListView.builder(
        itemCount: messages.length,
        itemBuilder: (context, index) =>
            _ChatBubble.fromMessage(messages[index]),
        controller: _scrollController,
      );
}

class _ChatBubble extends StatelessWidget {
  const _ChatBubble({
    this.text,
    this.author,
    this.backgroundColor,
    this.textColor,
    this.authorTextColor,
    this.alignment,
  });

  final String text;
  final String author;
  final Color backgroundColor;
  final Color textColor;
  final Color authorTextColor;
  final Alignment alignment;

  _ChatBubble.fromMessage(_Message message)
      : this(
          text: message.text,
          author: message.side.value == 0 ? "Xanho" : "You",
          backgroundColor: message.side.value == 0
              ? Colors.grey[700]
              : Colors.lightGreen[400],
          textColor:
              message.side.value == 0 ? Colors.grey[100] : Colors.grey[900],
          authorTextColor: message.side.value == 0
              ? Colors.grey[400]
              : Colors.lightGreen[900],
          alignment: message.side.value == 0
              ? Alignment.centerLeft
              : Alignment.centerRight,
        );

  @override
  Widget build(BuildContext context) {
    return FractionallySizedBox(
      widthFactor: 0.7,
      alignment: alignment,
      child: Container(
        margin: EdgeInsets.all(10),
        decoration: BoxDecoration(
          color: backgroundColor,
          borderRadius: BorderRadius.all(Radius.circular(5)),
        ),
        child: Container(
          child: Padding(
            padding: EdgeInsets.all(20),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  author + ":",
                  style: TextStyle(color: authorTextColor),
                  textAlign: TextAlign.left,
                ),
                Divider(
                  height: 8,
                  thickness: 1,
                  color: authorTextColor,
                ),
                Text(
                  text,
                  style: TextStyle(color: textColor),
                  textAlign: TextAlign.left,
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class _Message {
  const _Message(this.text, this.side);

  final String text;
  final _MessageSide side;
}

class _MessageSide {
  const _MessageSide(this.value);

  final int value;

  static final _MessageSide left = const _MessageSide(0);
  static final _MessageSide right = const _MessageSide(1);
}

class _Lipsum {
  static const List<String> paragraphs = [
    "Phasellus viverra velit id nunc tempus, eget condimentum nulla scelerisque. Aliquam imperdiet eros sed sollicitudin viverra. Duis at interdum nunc. Morbi id ornare nisl, sed efficitur est. Suspendisse bibendum quam ac velit facilisis dictum. Curabitur in hendrerit orci, molestie fringilla lacus. Pellentesque ac nisi fringilla, pharetra augue tincidunt, congue ex. In euismod, sem non gravida tempus, lectus massa iaculis metus, eu eleifend justo metus fringilla nisi. In non laoreet libero. Maecenas lacinia leo eget odio pretium, nec porttitor dui ultricies. Proin eget molestie nisi, quis placerat nisi.",
    "Sed commodo ante quam, vitae dictum sem ultrices ut. Suspendisse mollis scelerisque lacus sed eleifend. In finibus mauris ac est vehicula mollis. In non sodales mauris. Nullam id enim ut nisi ultrices lacinia et non mi. Vestibulum tincidunt orci nisi, vel cursus nulla accumsan vitae. Aenean non leo varius, lobortis nunc eget, consequat sapien. Curabitur viverra orci vitae tempus bibendum.",
    "Nullam blandit massa ac rhoncus imperdiet. Ut vitae ornare elit, ac posuere tortor. Etiam ut ultrices dui. Phasellus et odio quis dolor mollis facilisis. Mauris eu mattis sapien, at hendrerit justo. Maecenas id arcu vel mi condimentum ultrices. Duis sagittis condimentum nulla, eu vestibulum ipsum congue eget. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Cras cursus libero in eros varius, nec eleifend risus condimentum.",
    "Etiam iaculis nulla gravida, sollicitudin nunc ut, luctus nunc. Vivamus odio sem, placerat nec sodales eget, suscipit at nibh. Suspendisse feugiat consequat velit, eu malesuada urna aliquet semper. Aenean ullamcorper lectus mattis gravida dignissim. Nam posuere luctus nisi, et accumsan neque pretium vitae. Phasellus et arcu turpis. Etiam id faucibus magna. Suspendisse pharetra metus sit amet aliquet vulputate. Pellentesque ultricies laoreet semper. Duis id vestibulum ipsum, in porta ex.",
    "In hac habitasse platea dictumst. Sed euismod, odio at consequat semper, augue sapien fermentum justo, id tincidunt ante libero a lorem. Quisque finibus nulla vitae volutpat tempor. Nunc eget diam felis. Morbi eros ante, efficitur at dictum nec, posuere id ipsum. Maecenas pharetra tortor ipsum, ac molestie erat ultrices convallis. Mauris eget nisi at mi egestas faucibus quis eget ligula. Nullam congue nisi ac dui aliquet, eu aliquet justo fermentum. Pellentesque consectetur purus at erat posuere, at egestas tellus ornare.",
    "Suspendisse eu commodo orci, ac ultrices purus. Duis molestie at magna sed accumsan. Curabitur vel cursus mi. Cras augue lectus, convallis id consequat eu, congue nec augue. Cras ullamcorper et augue non tempus. Nulla ac velit vitae metus sollicitudin facilisis eu sit amet felis. Proin euismod facilisis arcu eu sagittis. Cras imperdiet mattis velit, et ornare nisl porttitor ut. Pellentesque at massa malesuada, scelerisque mauris sit amet, ultricies quam. Cras in nisi accumsan, tincidunt massa a, dapibus diam. Donec metus leo, viverra eu iaculis ac, lobortis in nibh. Donec gravida enim ut metus varius venenatis. Phasellus volutpat nulla at augue hendrerit, sit amet vestibulum lorem blandit.",
    "Aliquam laoreet leo vel ligula egestas scelerisque. Sed lacinia urna ex, id interdum quam vulputate nec. Aliquam ut quam orci. Nullam sit amet nisl tincidunt, lacinia ex eu, interdum nisi. Sed ac est varius mauris pulvinar ultricies non nec velit. Aliquam libero velit, vulputate sit amet consectetur efficitur, faucibus eu diam. In malesuada ac ante iaculis volutpat.",
    "In convallis dolor at ligula aliquam malesuada. Aenean eu augue massa. Vestibulum hendrerit tristique dui, nec aliquet eros vehicula non. Curabitur imperdiet nec risus non tempus. Nam facilisis sollicitudin nisi quis lobortis. Quisque placerat ante at vehicula sodales. Vestibulum laoreet dui velit, at viverra elit dignissim at. Proin interdum massa et vehicula ultricies. Vestibulum tincidunt vel tellus at volutpat. Curabitur nulla nibh, fringilla vitae blandit nec, vehicula non massa. Integer odio nunc, egestas et ipsum a, laoreet ultrices ex. Donec semper bibendum enim, vel lobortis nisi. Duis ut odio efficitur, tristique libero et, posuere augue. Nunc mollis elit felis, id facilisis erat aliquam vel. Duis ac quam fermentum, vehicula magna eget, congue nisi. Donec porttitor arcu non justo lobortis, congue varius felis ultrices.",
    "Ut suscipit, sem a gravida mollis, augue ex facilisis ante, at imperdiet ipsum arcu ut libero. Vestibulum nunc orci, rhoncus ut elementum nec, porttitor eget mi. Nullam sit amet nisl consequat, dignissim urna lacinia, tristique orci. Maecenas gravida eget libero non consequat. Donec placerat suscipit nisi in rutrum. Nam pellentesque tincidunt diam, ac egestas erat venenatis ut. Mauris sodales quam non pellentesque iaculis. Ut ut lorem lacinia, maximus diam ut, vulputate dui.",
    "Donec varius diam at maximus placerat. Nam sit amet erat nulla. Sed consequat augue eros, sed consequat purus sollicitudin vel. Proin ut sagittis justo. Nullam vitae elementum magna. Sed ipsum magna, luctus non convallis et, ullamcorper sed odio. Pellentesque vitae sodales ex. Donec congue, odio a scelerisque mattis, lectus arcu mattis dui, non sagittis orci mauris eu dolor. Aenean maximus feugiat metus vehicula molestie. Fusce et libero ac dolor congue viverra vel id mauris.",
    "Donec quam ipsum, imperdiet sagittis tincidunt et, sollicitudin a lorem. Curabitur tempus augue pharetra nisl facilisis volutpat. Suspendisse sollicitudin mi ac imperdiet mattis. Proin fermentum lobortis magna quis viverra. Morbi facilisis magna enim, sit amet pulvinar magna porttitor eget. Suspendisse vitae accumsan lectus. Fusce ultrices vitae ligula sed blandit. Vestibulum ac sapien lobortis, tincidunt arcu sit amet, vehicula sapien. Duis ipsum augue, bibendum vitae tortor non, congue scelerisque eros. Suspendisse ex nisi, malesuada vel nibh vel, feugiat gravida tortor. Fusce eu tortor efficitur, scelerisque ligula a, sollicitudin orci. Aliquam eget lectus eget ex lobortis maximus et id nisi.",
  ];
}

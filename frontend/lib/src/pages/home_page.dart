import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import './chat_page.dart';

class HomePage extends StatefulWidget {
  HomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: ConstrainedBox(
          constraints: BoxConstraints(maxWidth: 720),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text(
                'Get started.',
              ),
              _goButton(context),
            ],
          ),
        ),
      ),
    );
  }

  _goButton(BuildContext context) {
    final onPressed = () => Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => ChatPage(_defaultUserGraphId(context)),
          ),
        );
    return IconButton(
      onPressed: onPressed,
      icon: Icon(Icons.navigate_next_rounded),
    );
  }

  _defaultUserGraphId(BuildContext context) =>
      "user-graph-" + context.watch<User>().uid;
}

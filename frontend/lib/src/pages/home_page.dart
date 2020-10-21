import 'package:flutter/material.dart';
import 'package:uuid/uuid.dart';

import './graph_page.dart';

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
              GraphIdForm(),
            ],
          ),
        ),
      ),
    );
  }
}

class GraphIdForm extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _GraphIdFormState();
}

class _GraphIdFormState extends State<GraphIdForm> {
  @override
  Widget build(BuildContext context) {
    return IconButton(
      onPressed: () {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => GraphPage(
              Uuid().v4(),
            ),
          ),
        );
      },
      icon: Icon(Icons.navigate_next_rounded),
    );
  }
}

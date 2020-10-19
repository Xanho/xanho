import 'package:flutter/material.dart';

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
                'Enter a Graph ID to get started.',
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
  final _formKey = GlobalKey<FormState>();

  final _graphIdFieldController = TextEditingController();

  @override
  Widget build(BuildContext context) {
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
            controller: _graphIdFieldController,
          ),
          IconButton(
            onPressed: () {
              if (_formKey.currentState.validate()) {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) =>
                        GraphPage(_graphIdFieldController.text),
                  ),
                );
              }
            },
            icon: Icon(Icons.navigate_next_rounded),
          )
        ]));
  }
}

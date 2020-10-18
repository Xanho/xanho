import 'package:flutter/material.dart';

import 'package:frontend/src/pages/home_page.dart';
import 'package:provider/provider.dart';

import 'src/logic/graph_service.dart';

void main() {
  runApp(App());
}

class App extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Xanho',
      theme: ThemeData(
        brightness: Brightness.dark,
        primarySwatch: Colors.green,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: Provider<GraphService>(
        create: (_) => GraphService(),
        child: HomePage(title: 'Xanho'),
      ),
    );
  }
}

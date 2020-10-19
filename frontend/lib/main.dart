import 'package:flutter/material.dart';

import 'package:frontend/src/pages/home_page.dart';
import 'package:frontend/src/pages/loading_page.dart';
import 'package:provider/provider.dart';

import 'src/logic/graph_service.dart';

import 'package:firebase_core/firebase_core.dart';

void main() {
  runApp(App());
}

class App extends StatelessWidget {
  final Future<FirebaseApp> _initialization = Firebase.initializeApp();

  static final themeData = ThemeData(
    brightness: Brightness.dark,
    primarySwatch: Colors.green,
    visualDensity: VisualDensity.adaptivePlatformDensity,
  );

  @override
  Widget build(BuildContext context) {
    final futureBuilder = FutureBuilder(
      future: _initialization,
      builder: _futureBuilderF,
    );

    return _app(context, futureBuilder);
  }

  Widget _futureBuilderF(
      BuildContext context, AsyncSnapshot<FirebaseApp> snapshot) {
    if (snapshot.hasError) {
      return LoadingPage();
    }

    if (snapshot.connectionState == ConnectionState.done) {
      return _home(context);
    }

    return LoadingPage();
  }

  _home(BuildContext context) {
    final homePage = HomePage(title: 'Xanho');

    final provider = Provider<GraphService>(
      create: (_) => GraphService(),
      child: homePage,
    );

    return provider;
  }

  _app(BuildContext context, Widget home) {
    final materialApp = MaterialApp(
      title: 'Xanho',
      theme: themeData,
      home: home,
    );

    return materialApp;
  }
}

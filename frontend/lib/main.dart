import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:frontend/src/pages/auth_page.dart';

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
      return _app(context, LoadingPage());
    }

    if (snapshot.connectionState == ConnectionState.done) {
      return _app(context, _authRoot(context));
    }

    return _app(context, LoadingPage());
  }

  _authRoot(BuildContext context) {
    final stream = FirebaseAuth.instance.authStateChanges();

    return StreamBuilder<User>(
      stream: stream,
      builder: (context, snapshot) => snapshot.hasData
          ? _provide(
              context,
              snapshot.data,
              _provide(
                context,
                GraphService(),
                _app(context, _home(context)),
              ),
            )
          : AuthPage(),
    );
  }

  _provide<T>(BuildContext context, T t, Widget child) {
    return Provider<T>(
      create: (_) => t,
      child: child,
    );
  }

  _home(BuildContext context) => HomePage(title: 'Xanho');

  _app(BuildContext context, Widget home) {
    final materialApp = MaterialApp(
      title: 'Xanho',
      theme: themeData,
      home: home,
    );

    return materialApp;
  }
}

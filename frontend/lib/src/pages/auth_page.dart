import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';

class AuthPage extends StatefulWidget {
  AuthPage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _AuthPageState createState() => _AuthPageState();
}

class _AuthPageState extends State<AuthPage> {
  @override
  Widget build(BuildContext context) {
    final googleAuthButton = Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Text("Google: "),
        _googleAuthButton(context),
      ],
    );
    final anonymousAuthButton = Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Text("Anonymous: "),
        _anonymousAuthButton(context),
      ],
    );
    return Scaffold(
      body: Center(
        child: ConstrainedBox(
          constraints: BoxConstraints(maxWidth: 720),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text(
                'Login.',
              ),
              googleAuthButton,
              anonymousAuthButton,
            ],
          ),
        ),
      ),
    );
  }

  _googleAuthButton(BuildContext context) {
    final googleAuthProvider = GoogleAuthProvider();
    final onClick = () {
      FirebaseAuth.instance.signInWithPopup(googleAuthProvider);
    };

    return IconButton(
      onPressed: onClick,
      icon: Icon(Icons.login),
    );
  }

  _anonymousAuthButton(BuildContext context) {
    final onClick = () {
      FirebaseAuth.instance.signInAnonymously();
    };

    return IconButton(
      onPressed: onClick,
      icon: Icon(Icons.login),
    );
  }
}

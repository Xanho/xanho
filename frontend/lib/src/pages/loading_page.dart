import 'package:flutter/material.dart';

class LoadingPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final progressCircle = CircularProgressIndicator();

    final column = Column(
      mainAxisAlignment: MainAxisAlignment.center,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [Center(child: progressCircle)],
    );

    return Scaffold(
      body: column,
    );
  }
}

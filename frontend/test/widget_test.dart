import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:frontend/main.dart';

void main() {
  testWidgets('Ask for a graph ID', (WidgetTester tester) async {
    await tester.pumpWidget(App());

    expect(find.text("Enter a Graph ID to get started."), findsOneWidget);
  });
}

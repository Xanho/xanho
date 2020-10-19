import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

import '../lib/src/widgets/chat.dart';

void main() {
  testWidgets('ChatBubble Widget', (WidgetTester tester) async {
    var widget = ChatBubble.fromMessage(
      ChatBubbleMessage(
        "Hello world.",
        ChatBubbleMessageSide.right,
      ),
    );

    var app = MaterialApp(home: widget);

    await tester.pumpWidget(app);

    expect(find.text("Hello world."), findsOneWidget);
  });
}

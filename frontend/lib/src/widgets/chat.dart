import 'package:flutter/material.dart';

class ChatBubble extends StatelessWidget {
  const ChatBubble({
    this.text,
    this.author,
    this.backgroundColor,
    this.textColor,
    this.authorTextColor,
    this.alignment,
  });

  final String text;
  final String author;
  final Color backgroundColor;
  final Color textColor;
  final Color authorTextColor;
  final Alignment alignment;

  ChatBubble.fromMessage(Message message)
      : this(
          text: message.text,
          author: message.side.value == 0 ? "Xanho" : "You",
          backgroundColor: message.side.value == 0
              ? Colors.grey[700]
              : Colors.lightGreen[400],
          textColor:
              message.side.value == 0 ? Colors.grey[100] : Colors.grey[900],
          authorTextColor: message.side.value == 0
              ? Colors.grey[400]
              : Colors.lightGreen[900],
          alignment: message.side.value == 0
              ? Alignment.centerLeft
              : Alignment.centerRight,
        );

  @override
  Widget build(BuildContext context) {
    return FractionallySizedBox(
      widthFactor: 0.7,
      alignment: alignment,
      child: Container(
        margin: EdgeInsets.all(10),
        decoration: BoxDecoration(
          color: backgroundColor,
          borderRadius: BorderRadius.all(Radius.circular(5)),
        ),
        child: Container(
          child: Padding(
            padding: EdgeInsets.all(20),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  author + ":",
                  style: TextStyle(color: authorTextColor),
                  textAlign: TextAlign.left,
                ),
                Divider(
                  height: 8,
                  thickness: 1,
                  color: authorTextColor,
                ),
                Text(
                  text,
                  style: TextStyle(color: textColor),
                  textAlign: TextAlign.left,
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class Message {
  const Message(this.text, this.side);

  final String text;
  final MessageSide side;
}

class MessageSide {
  const MessageSide(this.value);

  final int value;

  static final MessageSide left = const MessageSide(0);
  static final MessageSide right = const MessageSide(1);
}

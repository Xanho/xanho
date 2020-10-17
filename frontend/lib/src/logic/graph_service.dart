import 'package:grpc/grpc.dart';

import 'package:frontend/src/proto/org/xanho/proto/service/knowledgeGraph.pbgrpc.dart';
import 'package:frontend/src/proto/org/xanho/proto/nlp/nlp.pb.dart';

class GraphService {
  GraphService() {
    this._channel = ClientChannel('backend.xanho.org');

    this._stub = KnowledgeGraphServiceClient(this._channel);
  }

  ClientChannel _channel;

  KnowledgeGraphServiceClient _stub;

  Future<IngestTextStreamResponse> sendMessage(String graphId, String text) {
    final request = IngestTextRequest()
      ..graphId = graphId
      ..text = text;
    final res = _stub
        .ingestTextStream(Stream.value(request))
        .timeout(Duration(seconds: 5));

    res.whenComplete(() => print("Done"));
    return res;
  }

  Future<String> receiveMessage(String graphId) {
    return _stub
        .generateResponse(
          GenerateResponseRequest()..graphId = graphId,
        )
        .then((res) => _documentToString(res.document))
        .then((str) {
      print(str);
      return str;
    });
  }

  String _documentToString(Document document) {
    return document.paragraphs
        .map((paragraph) => paragraph.sentences
            .map(
              (sentence) =>
                  sentence.phrase.words.map((w) => w.value).join(" ") +
                  sentence.punctuation.value,
            )
            .join(" "))
        .join("\n\n");
  }
}

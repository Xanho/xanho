import 'package:frontend/src/proto/org/xanho/proto/graph/graph.pb.dart';
import 'package:frontend/src/proto/org/xanho/proto/service/knowledgeGraph.pbgrpc.dart';

import '../proto/org/xanho/proto/service/knowledgeGraph.pb.dart';
import 'grpc_channel.dart' if (dart.library.html) 'grpc_web_channel.dart';

class GraphService {
  GraphService() {
    this._stub = KnowledgeGraphServiceClient(channel);
  }

  KnowledgeGraphServiceClient _stub;

  Stream<TextMessage> messagesStream(String graphId) => _stub
      .messagesStream(
        MessagesStreamRequest()..graphId = graphId,
      )
      .handleError(
        (_) {},
      );

  Future<bool> sendMessage(String graphId, TextMessage message) => _stub
      .sendTextMessage(
        SendTextMessageRequest()
          ..graphId = graphId
          ..message = message,
      )
      .then((res) => res.success);

  Future<Graph> getGraph(String graphId) => _stub
      .getGraph(GetGraphRequest()..graphId = graphId)
      .then((res) => res.graph);
}

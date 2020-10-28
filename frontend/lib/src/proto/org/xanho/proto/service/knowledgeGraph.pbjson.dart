///
//  Generated code. Do not modify.
//  source: org/xanho/proto/service/knowledgeGraph.proto
//
// @dart = 2.3
// ignore_for_file: camel_case_types,non_constant_identifier_names,library_prefixes,unused_import,unused_shown_name,return_of_invalid_type

const MessageSource$json = const {
  '1': 'MessageSource',
  '2': const [
    const {'1': 'USER', '2': 0},
    const {'1': 'SYSTEM', '2': 1},
  ],
};

const MessagesStreamRequest$json = const {
  '1': 'MessagesStreamRequest',
  '2': const [
    const {'1': 'graphId', '3': 1, '4': 1, '5': 9, '10': 'graphId'},
  ],
};

const TextMessage$json = const {
  '1': 'TextMessage',
  '2': const [
    const {'1': 'id', '3': 1, '4': 1, '5': 9, '10': 'id'},
    const {'1': 'source', '3': 2, '4': 1, '5': 14, '6': '.org.xanho.proto.service.knowledgegraph.MessageSource', '10': 'source'},
    const {'1': 'timestampMs', '3': 3, '4': 1, '5': 3, '10': 'timestampMs'},
    const {'1': 'text', '3': 4, '4': 1, '5': 9, '10': 'text'},
  ],
};

const SendTextMessageRequest$json = const {
  '1': 'SendTextMessageRequest',
  '2': const [
    const {'1': 'graphId', '3': 1, '4': 1, '5': 9, '10': 'graphId'},
    const {'1': 'message', '3': 2, '4': 1, '5': 11, '6': '.org.xanho.proto.service.knowledgegraph.TextMessage', '10': 'message'},
  ],
};

const SendTextMessageResponse$json = const {
  '1': 'SendTextMessageResponse',
  '2': const [
    const {'1': 'graphId', '3': 1, '4': 1, '5': 9, '10': 'graphId'},
    const {'1': 'success', '3': 2, '4': 1, '5': 8, '10': 'success'},
  ],
};

const GetGraphRequest$json = const {
  '1': 'GetGraphRequest',
  '2': const [
    const {'1': 'graphId', '3': 1, '4': 1, '5': 9, '10': 'graphId'},
  ],
};

const GetGraphResponse$json = const {
  '1': 'GetGraphResponse',
  '2': const [
    const {'1': 'graphId', '3': 1, '4': 1, '5': 9, '10': 'graphId'},
    const {'1': 'graph', '3': 2, '4': 1, '5': 11, '6': '.org.xanho.proto.graph.Graph', '10': 'graph'},
  ],
};


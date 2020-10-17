///
//  Generated code. Do not modify.
//  source: org/xanho/proto/service/knowledgeGraph.proto
//
// @dart = 2.3
// ignore_for_file: camel_case_types,non_constant_identifier_names,library_prefixes,unused_import,unused_shown_name,return_of_invalid_type

const IngestTextRequest$json = const {
  '1': 'IngestTextRequest',
  '2': const [
    const {'1': 'graphId', '3': 1, '4': 1, '5': 9, '10': 'graphId'},
    const {'1': 'text', '3': 2, '4': 1, '5': 9, '10': 'text'},
  ],
};

const IngestTextStreamResponse$json = const {
  '1': 'IngestTextStreamResponse',
  '2': const [
    const {'1': 'processedCount', '3': 1, '4': 1, '5': 5, '10': 'processedCount'},
  ],
};

const GetAnalysisRequest$json = const {
  '1': 'GetAnalysisRequest',
  '2': const [
    const {'1': 'graphId', '3': 1, '4': 1, '5': 9, '10': 'graphId'},
  ],
};

const GetAnalysisResponse$json = const {
  '1': 'GetAnalysisResponse',
  '2': const [
    const {'1': 'graphId', '3': 1, '4': 1, '5': 9, '10': 'graphId'},
    const {'1': 'vocabulary', '3': 101, '4': 3, '5': 11, '6': '.org.xanho.proto.nlp.Token.Word', '10': 'vocabulary'},
    const {'1': 'wordFrequencies', '3': 102, '4': 3, '5': 11, '6': '.org.xanho.proto.service.knowledgegraph.GetAnalysisResponse.WordFrequency', '10': 'wordFrequencies'},
  ],
  '3': const [GetAnalysisResponse_WordFrequency$json],
};

const GetAnalysisResponse_WordFrequency$json = const {
  '1': 'WordFrequency',
  '2': const [
    const {'1': 'word', '3': 1, '4': 1, '5': 11, '6': '.org.xanho.proto.nlp.Token.Word', '10': 'word'},
    const {'1': 'percent', '3': 2, '4': 1, '5': 1, '10': 'percent'},
  ],
};

const GenerateResponseRequest$json = const {
  '1': 'GenerateResponseRequest',
  '2': const [
    const {'1': 'graphId', '3': 1, '4': 1, '5': 9, '10': 'graphId'},
  ],
};

const GenerateResponseResponse$json = const {
  '1': 'GenerateResponseResponse',
  '2': const [
    const {'1': 'graphId', '3': 1, '4': 1, '5': 9, '10': 'graphId'},
    const {'1': 'document', '3': 2, '4': 1, '5': 11, '6': '.org.xanho.proto.nlp.Document', '10': 'document'},
  ],
};


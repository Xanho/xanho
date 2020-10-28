///
//  Generated code. Do not modify.
//  source: org/xanho/proto/graph/graph.proto
//
// @dart = 2.3
// ignore_for_file: camel_case_types,non_constant_identifier_names,library_prefixes,unused_import,unused_shown_name,return_of_invalid_type

const NullData$json = const {
  '1': 'NullData',
  '2': const [
    const {'1': 'NULL_DATA', '2': 0},
  ],
};

const Node$json = const {
  '1': 'Node',
  '2': const [
    const {'1': 'id', '3': 1, '4': 1, '5': 5, '10': 'id'},
    const {'1': 'nodeType', '3': 2, '4': 1, '5': 9, '10': 'nodeType'},
    const {'1': 'data', '3': 3, '4': 1, '5': 11, '6': '.org.xanho.proto.graph.DataObject', '10': 'data'},
  ],
};

const Edge$json = const {
  '1': 'Edge',
  '2': const [
    const {'1': 'id', '3': 1, '4': 1, '5': 5, '10': 'id'},
    const {'1': 'edgeType', '3': 2, '4': 1, '5': 9, '10': 'edgeType'},
    const {'1': 'sourceId', '3': 3, '4': 1, '5': 5, '10': 'sourceId'},
    const {'1': 'destinationId', '3': 4, '4': 1, '5': 5, '10': 'destinationId'},
    const {'1': 'data', '3': 5, '4': 1, '5': 11, '6': '.org.xanho.proto.graph.DataObject', '10': 'data'},
  ],
};

const Graph$json = const {
  '1': 'Graph',
  '2': const [
    const {'1': 'nodes', '3': 1, '4': 3, '5': 11, '6': '.org.xanho.proto.graph.Node', '10': 'nodes'},
    const {'1': 'edges', '3': 2, '4': 3, '5': 11, '6': '.org.xanho.proto.graph.Edge', '10': 'edges'},
  ],
};

const DataObject$json = const {
  '1': 'DataObject',
  '2': const [
    const {'1': 'values', '3': 1, '4': 3, '5': 11, '6': '.org.xanho.proto.graph.DataObject.ValuesEntry', '10': 'values'},
  ],
  '3': const [DataObject_ValuesEntry$json],
};

const DataObject_ValuesEntry$json = const {
  '1': 'ValuesEntry',
  '2': const [
    const {'1': 'key', '3': 1, '4': 1, '5': 9, '10': 'key'},
    const {'1': 'value', '3': 2, '4': 1, '5': 11, '6': '.org.xanho.proto.graph.Data', '10': 'value'},
  ],
  '7': const {'7': true},
};

const DataList$json = const {
  '1': 'DataList',
  '2': const [
    const {'1': 'values', '3': 1, '4': 3, '5': 11, '6': '.org.xanho.proto.graph.Data', '10': 'values'},
  ],
};

const Data$json = const {
  '1': 'Data',
  '2': const [
    const {'1': 'boolValue', '3': 1, '4': 1, '5': 8, '9': 0, '10': 'boolValue'},
    const {'1': 'intValue', '3': 2, '4': 1, '5': 5, '9': 0, '10': 'intValue'},
    const {'1': 'longValue', '3': 3, '4': 1, '5': 3, '9': 0, '10': 'longValue'},
    const {'1': 'floatValue', '3': 4, '4': 1, '5': 2, '9': 0, '10': 'floatValue'},
    const {'1': 'doubleValue', '3': 5, '4': 1, '5': 1, '9': 0, '10': 'doubleValue'},
    const {'1': 'stringValue', '3': 6, '4': 1, '5': 9, '9': 0, '10': 'stringValue'},
    const {'1': 'nullValue', '3': 7, '4': 1, '5': 14, '6': '.org.xanho.proto.graph.NullData', '9': 0, '10': 'nullValue'},
    const {'1': 'objectValue', '3': 8, '4': 1, '5': 11, '6': '.org.xanho.proto.graph.DataObject', '9': 0, '10': 'objectValue'},
    const {'1': 'listValue', '3': 9, '4': 1, '5': 11, '6': '.org.xanho.proto.graph.DataList', '9': 0, '10': 'listValue'},
  ],
  '8': const [
    const {'1': 'kind'},
  ],
};


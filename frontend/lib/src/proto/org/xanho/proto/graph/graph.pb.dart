///
//  Generated code. Do not modify.
//  source: org/xanho/proto/graph/graph.proto
//
// @dart = 2.3
// ignore_for_file: camel_case_types,non_constant_identifier_names,library_prefixes,unused_import,unused_shown_name,return_of_invalid_type

import 'dart:core' as $core;

import 'package:fixnum/fixnum.dart' as $fixnum;
import 'package:protobuf/protobuf.dart' as $pb;

import 'graph.pbenum.dart';

export 'graph.pbenum.dart';

class Node extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('Node', package: const $pb.PackageName('org.xanho.proto.graph'), createEmptyInstance: create)
    ..a<$core.int>(1, 'id', $pb.PbFieldType.O3)
    ..aOS(2, 'nodeType', protoName: 'nodeType')
    ..aOM<DataObject>(3, 'data', subBuilder: DataObject.create)
    ..hasRequiredFields = false
  ;

  Node._() : super();
  factory Node() => create();
  factory Node.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory Node.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  Node clone() => Node()..mergeFromMessage(this);
  Node copyWith(void Function(Node) updates) => super.copyWith((message) => updates(message as Node));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static Node create() => Node._();
  Node createEmptyInstance() => create();
  static $pb.PbList<Node> createRepeated() => $pb.PbList<Node>();
  @$core.pragma('dart2js:noInline')
  static Node getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<Node>(create);
  static Node _defaultInstance;

  @$pb.TagNumber(1)
  $core.int get id => $_getIZ(0);
  @$pb.TagNumber(1)
  set id($core.int v) { $_setSignedInt32(0, v); }
  @$pb.TagNumber(1)
  $core.bool hasId() => $_has(0);
  @$pb.TagNumber(1)
  void clearId() => clearField(1);

  @$pb.TagNumber(2)
  $core.String get nodeType => $_getSZ(1);
  @$pb.TagNumber(2)
  set nodeType($core.String v) { $_setString(1, v); }
  @$pb.TagNumber(2)
  $core.bool hasNodeType() => $_has(1);
  @$pb.TagNumber(2)
  void clearNodeType() => clearField(2);

  @$pb.TagNumber(3)
  DataObject get data => $_getN(2);
  @$pb.TagNumber(3)
  set data(DataObject v) { setField(3, v); }
  @$pb.TagNumber(3)
  $core.bool hasData() => $_has(2);
  @$pb.TagNumber(3)
  void clearData() => clearField(3);
  @$pb.TagNumber(3)
  DataObject ensureData() => $_ensure(2);
}

class Edge extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('Edge', package: const $pb.PackageName('org.xanho.proto.graph'), createEmptyInstance: create)
    ..a<$core.int>(1, 'id', $pb.PbFieldType.O3)
    ..aOS(2, 'edgeType', protoName: 'edgeType')
    ..a<$core.int>(3, 'sourceId', $pb.PbFieldType.O3, protoName: 'sourceId')
    ..a<$core.int>(4, 'destinationId', $pb.PbFieldType.O3, protoName: 'destinationId')
    ..aOM<DataObject>(5, 'data', subBuilder: DataObject.create)
    ..hasRequiredFields = false
  ;

  Edge._() : super();
  factory Edge() => create();
  factory Edge.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory Edge.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  Edge clone() => Edge()..mergeFromMessage(this);
  Edge copyWith(void Function(Edge) updates) => super.copyWith((message) => updates(message as Edge));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static Edge create() => Edge._();
  Edge createEmptyInstance() => create();
  static $pb.PbList<Edge> createRepeated() => $pb.PbList<Edge>();
  @$core.pragma('dart2js:noInline')
  static Edge getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<Edge>(create);
  static Edge _defaultInstance;

  @$pb.TagNumber(1)
  $core.int get id => $_getIZ(0);
  @$pb.TagNumber(1)
  set id($core.int v) { $_setSignedInt32(0, v); }
  @$pb.TagNumber(1)
  $core.bool hasId() => $_has(0);
  @$pb.TagNumber(1)
  void clearId() => clearField(1);

  @$pb.TagNumber(2)
  $core.String get edgeType => $_getSZ(1);
  @$pb.TagNumber(2)
  set edgeType($core.String v) { $_setString(1, v); }
  @$pb.TagNumber(2)
  $core.bool hasEdgeType() => $_has(1);
  @$pb.TagNumber(2)
  void clearEdgeType() => clearField(2);

  @$pb.TagNumber(3)
  $core.int get sourceId => $_getIZ(2);
  @$pb.TagNumber(3)
  set sourceId($core.int v) { $_setSignedInt32(2, v); }
  @$pb.TagNumber(3)
  $core.bool hasSourceId() => $_has(2);
  @$pb.TagNumber(3)
  void clearSourceId() => clearField(3);

  @$pb.TagNumber(4)
  $core.int get destinationId => $_getIZ(3);
  @$pb.TagNumber(4)
  set destinationId($core.int v) { $_setSignedInt32(3, v); }
  @$pb.TagNumber(4)
  $core.bool hasDestinationId() => $_has(3);
  @$pb.TagNumber(4)
  void clearDestinationId() => clearField(4);

  @$pb.TagNumber(5)
  DataObject get data => $_getN(4);
  @$pb.TagNumber(5)
  set data(DataObject v) { setField(5, v); }
  @$pb.TagNumber(5)
  $core.bool hasData() => $_has(4);
  @$pb.TagNumber(5)
  void clearData() => clearField(5);
  @$pb.TagNumber(5)
  DataObject ensureData() => $_ensure(4);
}

class Graph extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('Graph', package: const $pb.PackageName('org.xanho.proto.graph'), createEmptyInstance: create)
    ..pc<Node>(1, 'nodes', $pb.PbFieldType.PM, subBuilder: Node.create)
    ..pc<Edge>(2, 'edges', $pb.PbFieldType.PM, subBuilder: Edge.create)
    ..hasRequiredFields = false
  ;

  Graph._() : super();
  factory Graph() => create();
  factory Graph.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory Graph.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  Graph clone() => Graph()..mergeFromMessage(this);
  Graph copyWith(void Function(Graph) updates) => super.copyWith((message) => updates(message as Graph));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static Graph create() => Graph._();
  Graph createEmptyInstance() => create();
  static $pb.PbList<Graph> createRepeated() => $pb.PbList<Graph>();
  @$core.pragma('dart2js:noInline')
  static Graph getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<Graph>(create);
  static Graph _defaultInstance;

  @$pb.TagNumber(1)
  $core.List<Node> get nodes => $_getList(0);

  @$pb.TagNumber(2)
  $core.List<Edge> get edges => $_getList(1);
}

class DataObject extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('DataObject', package: const $pb.PackageName('org.xanho.proto.graph'), createEmptyInstance: create)
    ..m<$core.String, Data>(1, 'values', entryClassName: 'DataObject.ValuesEntry', keyFieldType: $pb.PbFieldType.OS, valueFieldType: $pb.PbFieldType.OM, valueCreator: Data.create, packageName: const $pb.PackageName('org.xanho.proto.graph'))
    ..hasRequiredFields = false
  ;

  DataObject._() : super();
  factory DataObject() => create();
  factory DataObject.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory DataObject.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  DataObject clone() => DataObject()..mergeFromMessage(this);
  DataObject copyWith(void Function(DataObject) updates) => super.copyWith((message) => updates(message as DataObject));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static DataObject create() => DataObject._();
  DataObject createEmptyInstance() => create();
  static $pb.PbList<DataObject> createRepeated() => $pb.PbList<DataObject>();
  @$core.pragma('dart2js:noInline')
  static DataObject getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<DataObject>(create);
  static DataObject _defaultInstance;

  @$pb.TagNumber(1)
  $core.Map<$core.String, Data> get values => $_getMap(0);
}

class DataList extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('DataList', package: const $pb.PackageName('org.xanho.proto.graph'), createEmptyInstance: create)
    ..pc<Data>(1, 'values', $pb.PbFieldType.PM, subBuilder: Data.create)
    ..hasRequiredFields = false
  ;

  DataList._() : super();
  factory DataList() => create();
  factory DataList.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory DataList.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  DataList clone() => DataList()..mergeFromMessage(this);
  DataList copyWith(void Function(DataList) updates) => super.copyWith((message) => updates(message as DataList));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static DataList create() => DataList._();
  DataList createEmptyInstance() => create();
  static $pb.PbList<DataList> createRepeated() => $pb.PbList<DataList>();
  @$core.pragma('dart2js:noInline')
  static DataList getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<DataList>(create);
  static DataList _defaultInstance;

  @$pb.TagNumber(1)
  $core.List<Data> get values => $_getList(0);
}

enum Data_Kind {
  boolValue, 
  intValue, 
  longValue, 
  floatValue, 
  doubleValue, 
  stringValue, 
  nullValue, 
  objectValue, 
  listValue, 
  notSet
}

class Data extends $pb.GeneratedMessage {
  static const $core.Map<$core.int, Data_Kind> _Data_KindByTag = {
    1 : Data_Kind.boolValue,
    2 : Data_Kind.intValue,
    3 : Data_Kind.longValue,
    4 : Data_Kind.floatValue,
    5 : Data_Kind.doubleValue,
    6 : Data_Kind.stringValue,
    7 : Data_Kind.nullValue,
    8 : Data_Kind.objectValue,
    9 : Data_Kind.listValue,
    0 : Data_Kind.notSet
  };
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('Data', package: const $pb.PackageName('org.xanho.proto.graph'), createEmptyInstance: create)
    ..oo(0, [1, 2, 3, 4, 5, 6, 7, 8, 9])
    ..aOB(1, 'boolValue', protoName: 'boolValue')
    ..a<$core.int>(2, 'intValue', $pb.PbFieldType.O3, protoName: 'intValue')
    ..aInt64(3, 'longValue', protoName: 'longValue')
    ..a<$core.double>(4, 'floatValue', $pb.PbFieldType.OF, protoName: 'floatValue')
    ..a<$core.double>(5, 'doubleValue', $pb.PbFieldType.OD, protoName: 'doubleValue')
    ..aOS(6, 'stringValue', protoName: 'stringValue')
    ..e<NullData>(7, 'nullValue', $pb.PbFieldType.OE, protoName: 'nullValue', defaultOrMaker: NullData.NULL_DATA, valueOf: NullData.valueOf, enumValues: NullData.values)
    ..aOM<DataObject>(8, 'objectValue', protoName: 'objectValue', subBuilder: DataObject.create)
    ..aOM<DataList>(9, 'listValue', protoName: 'listValue', subBuilder: DataList.create)
    ..hasRequiredFields = false
  ;

  Data._() : super();
  factory Data() => create();
  factory Data.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory Data.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  Data clone() => Data()..mergeFromMessage(this);
  Data copyWith(void Function(Data) updates) => super.copyWith((message) => updates(message as Data));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static Data create() => Data._();
  Data createEmptyInstance() => create();
  static $pb.PbList<Data> createRepeated() => $pb.PbList<Data>();
  @$core.pragma('dart2js:noInline')
  static Data getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<Data>(create);
  static Data _defaultInstance;

  Data_Kind whichKind() => _Data_KindByTag[$_whichOneof(0)];
  void clearKind() => clearField($_whichOneof(0));

  @$pb.TagNumber(1)
  $core.bool get boolValue => $_getBF(0);
  @$pb.TagNumber(1)
  set boolValue($core.bool v) { $_setBool(0, v); }
  @$pb.TagNumber(1)
  $core.bool hasBoolValue() => $_has(0);
  @$pb.TagNumber(1)
  void clearBoolValue() => clearField(1);

  @$pb.TagNumber(2)
  $core.int get intValue => $_getIZ(1);
  @$pb.TagNumber(2)
  set intValue($core.int v) { $_setSignedInt32(1, v); }
  @$pb.TagNumber(2)
  $core.bool hasIntValue() => $_has(1);
  @$pb.TagNumber(2)
  void clearIntValue() => clearField(2);

  @$pb.TagNumber(3)
  $fixnum.Int64 get longValue => $_getI64(2);
  @$pb.TagNumber(3)
  set longValue($fixnum.Int64 v) { $_setInt64(2, v); }
  @$pb.TagNumber(3)
  $core.bool hasLongValue() => $_has(2);
  @$pb.TagNumber(3)
  void clearLongValue() => clearField(3);

  @$pb.TagNumber(4)
  $core.double get floatValue => $_getN(3);
  @$pb.TagNumber(4)
  set floatValue($core.double v) { $_setFloat(3, v); }
  @$pb.TagNumber(4)
  $core.bool hasFloatValue() => $_has(3);
  @$pb.TagNumber(4)
  void clearFloatValue() => clearField(4);

  @$pb.TagNumber(5)
  $core.double get doubleValue => $_getN(4);
  @$pb.TagNumber(5)
  set doubleValue($core.double v) { $_setDouble(4, v); }
  @$pb.TagNumber(5)
  $core.bool hasDoubleValue() => $_has(4);
  @$pb.TagNumber(5)
  void clearDoubleValue() => clearField(5);

  @$pb.TagNumber(6)
  $core.String get stringValue => $_getSZ(5);
  @$pb.TagNumber(6)
  set stringValue($core.String v) { $_setString(5, v); }
  @$pb.TagNumber(6)
  $core.bool hasStringValue() => $_has(5);
  @$pb.TagNumber(6)
  void clearStringValue() => clearField(6);

  @$pb.TagNumber(7)
  NullData get nullValue => $_getN(6);
  @$pb.TagNumber(7)
  set nullValue(NullData v) { setField(7, v); }
  @$pb.TagNumber(7)
  $core.bool hasNullValue() => $_has(6);
  @$pb.TagNumber(7)
  void clearNullValue() => clearField(7);

  @$pb.TagNumber(8)
  DataObject get objectValue => $_getN(7);
  @$pb.TagNumber(8)
  set objectValue(DataObject v) { setField(8, v); }
  @$pb.TagNumber(8)
  $core.bool hasObjectValue() => $_has(7);
  @$pb.TagNumber(8)
  void clearObjectValue() => clearField(8);
  @$pb.TagNumber(8)
  DataObject ensureObjectValue() => $_ensure(7);

  @$pb.TagNumber(9)
  DataList get listValue => $_getN(8);
  @$pb.TagNumber(9)
  set listValue(DataList v) { setField(9, v); }
  @$pb.TagNumber(9)
  $core.bool hasListValue() => $_has(8);
  @$pb.TagNumber(9)
  void clearListValue() => clearField(9);
  @$pb.TagNumber(9)
  DataList ensureListValue() => $_ensure(8);
}


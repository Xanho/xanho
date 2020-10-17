///
//  Generated code. Do not modify.
//  source: org/xanho/proto/service/knowledgeGraph.proto
//
// @dart = 2.3
// ignore_for_file: camel_case_types,non_constant_identifier_names,library_prefixes,unused_import,unused_shown_name,return_of_invalid_type

import 'dart:core' as $core;

import 'package:protobuf/protobuf.dart' as $pb;

import '../nlp/nlp.pb.dart' as $1;

class IngestTextRequest extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('IngestTextRequest', package: const $pb.PackageName('org.xanho.proto.service.knowledgegraph'), createEmptyInstance: create)
    ..aOS(1, 'graphId', protoName: 'graphId')
    ..aOS(2, 'text')
    ..hasRequiredFields = false
  ;

  IngestTextRequest._() : super();
  factory IngestTextRequest() => create();
  factory IngestTextRequest.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory IngestTextRequest.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  IngestTextRequest clone() => IngestTextRequest()..mergeFromMessage(this);
  IngestTextRequest copyWith(void Function(IngestTextRequest) updates) => super.copyWith((message) => updates(message as IngestTextRequest));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static IngestTextRequest create() => IngestTextRequest._();
  IngestTextRequest createEmptyInstance() => create();
  static $pb.PbList<IngestTextRequest> createRepeated() => $pb.PbList<IngestTextRequest>();
  @$core.pragma('dart2js:noInline')
  static IngestTextRequest getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<IngestTextRequest>(create);
  static IngestTextRequest _defaultInstance;

  @$pb.TagNumber(1)
  $core.String get graphId => $_getSZ(0);
  @$pb.TagNumber(1)
  set graphId($core.String v) { $_setString(0, v); }
  @$pb.TagNumber(1)
  $core.bool hasGraphId() => $_has(0);
  @$pb.TagNumber(1)
  void clearGraphId() => clearField(1);

  @$pb.TagNumber(2)
  $core.String get text => $_getSZ(1);
  @$pb.TagNumber(2)
  set text($core.String v) { $_setString(1, v); }
  @$pb.TagNumber(2)
  $core.bool hasText() => $_has(1);
  @$pb.TagNumber(2)
  void clearText() => clearField(2);
}

class IngestTextStreamResponse extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('IngestTextStreamResponse', package: const $pb.PackageName('org.xanho.proto.service.knowledgegraph'), createEmptyInstance: create)
    ..a<$core.int>(1, 'processedCount', $pb.PbFieldType.O3, protoName: 'processedCount')
    ..hasRequiredFields = false
  ;

  IngestTextStreamResponse._() : super();
  factory IngestTextStreamResponse() => create();
  factory IngestTextStreamResponse.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory IngestTextStreamResponse.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  IngestTextStreamResponse clone() => IngestTextStreamResponse()..mergeFromMessage(this);
  IngestTextStreamResponse copyWith(void Function(IngestTextStreamResponse) updates) => super.copyWith((message) => updates(message as IngestTextStreamResponse));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static IngestTextStreamResponse create() => IngestTextStreamResponse._();
  IngestTextStreamResponse createEmptyInstance() => create();
  static $pb.PbList<IngestTextStreamResponse> createRepeated() => $pb.PbList<IngestTextStreamResponse>();
  @$core.pragma('dart2js:noInline')
  static IngestTextStreamResponse getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<IngestTextStreamResponse>(create);
  static IngestTextStreamResponse _defaultInstance;

  @$pb.TagNumber(1)
  $core.int get processedCount => $_getIZ(0);
  @$pb.TagNumber(1)
  set processedCount($core.int v) { $_setSignedInt32(0, v); }
  @$pb.TagNumber(1)
  $core.bool hasProcessedCount() => $_has(0);
  @$pb.TagNumber(1)
  void clearProcessedCount() => clearField(1);
}

class GetAnalysisRequest extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('GetAnalysisRequest', package: const $pb.PackageName('org.xanho.proto.service.knowledgegraph'), createEmptyInstance: create)
    ..aOS(1, 'graphId', protoName: 'graphId')
    ..hasRequiredFields = false
  ;

  GetAnalysisRequest._() : super();
  factory GetAnalysisRequest() => create();
  factory GetAnalysisRequest.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory GetAnalysisRequest.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  GetAnalysisRequest clone() => GetAnalysisRequest()..mergeFromMessage(this);
  GetAnalysisRequest copyWith(void Function(GetAnalysisRequest) updates) => super.copyWith((message) => updates(message as GetAnalysisRequest));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static GetAnalysisRequest create() => GetAnalysisRequest._();
  GetAnalysisRequest createEmptyInstance() => create();
  static $pb.PbList<GetAnalysisRequest> createRepeated() => $pb.PbList<GetAnalysisRequest>();
  @$core.pragma('dart2js:noInline')
  static GetAnalysisRequest getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<GetAnalysisRequest>(create);
  static GetAnalysisRequest _defaultInstance;

  @$pb.TagNumber(1)
  $core.String get graphId => $_getSZ(0);
  @$pb.TagNumber(1)
  set graphId($core.String v) { $_setString(0, v); }
  @$pb.TagNumber(1)
  $core.bool hasGraphId() => $_has(0);
  @$pb.TagNumber(1)
  void clearGraphId() => clearField(1);
}

class GetAnalysisResponse_WordFrequency extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('GetAnalysisResponse.WordFrequency', package: const $pb.PackageName('org.xanho.proto.service.knowledgegraph'), createEmptyInstance: create)
    ..aOM<$1.Token_Word>(1, 'word', subBuilder: $1.Token_Word.create)
    ..a<$core.double>(2, 'percent', $pb.PbFieldType.OD)
    ..hasRequiredFields = false
  ;

  GetAnalysisResponse_WordFrequency._() : super();
  factory GetAnalysisResponse_WordFrequency() => create();
  factory GetAnalysisResponse_WordFrequency.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory GetAnalysisResponse_WordFrequency.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  GetAnalysisResponse_WordFrequency clone() => GetAnalysisResponse_WordFrequency()..mergeFromMessage(this);
  GetAnalysisResponse_WordFrequency copyWith(void Function(GetAnalysisResponse_WordFrequency) updates) => super.copyWith((message) => updates(message as GetAnalysisResponse_WordFrequency));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static GetAnalysisResponse_WordFrequency create() => GetAnalysisResponse_WordFrequency._();
  GetAnalysisResponse_WordFrequency createEmptyInstance() => create();
  static $pb.PbList<GetAnalysisResponse_WordFrequency> createRepeated() => $pb.PbList<GetAnalysisResponse_WordFrequency>();
  @$core.pragma('dart2js:noInline')
  static GetAnalysisResponse_WordFrequency getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<GetAnalysisResponse_WordFrequency>(create);
  static GetAnalysisResponse_WordFrequency _defaultInstance;

  @$pb.TagNumber(1)
  $1.Token_Word get word => $_getN(0);
  @$pb.TagNumber(1)
  set word($1.Token_Word v) { setField(1, v); }
  @$pb.TagNumber(1)
  $core.bool hasWord() => $_has(0);
  @$pb.TagNumber(1)
  void clearWord() => clearField(1);
  @$pb.TagNumber(1)
  $1.Token_Word ensureWord() => $_ensure(0);

  @$pb.TagNumber(2)
  $core.double get percent => $_getN(1);
  @$pb.TagNumber(2)
  set percent($core.double v) { $_setDouble(1, v); }
  @$pb.TagNumber(2)
  $core.bool hasPercent() => $_has(1);
  @$pb.TagNumber(2)
  void clearPercent() => clearField(2);
}

class GetAnalysisResponse extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('GetAnalysisResponse', package: const $pb.PackageName('org.xanho.proto.service.knowledgegraph'), createEmptyInstance: create)
    ..aOS(1, 'graphId', protoName: 'graphId')
    ..pc<$1.Token_Word>(101, 'vocabulary', $pb.PbFieldType.PM, subBuilder: $1.Token_Word.create)
    ..pc<GetAnalysisResponse_WordFrequency>(102, 'wordFrequencies', $pb.PbFieldType.PM, protoName: 'wordFrequencies', subBuilder: GetAnalysisResponse_WordFrequency.create)
    ..hasRequiredFields = false
  ;

  GetAnalysisResponse._() : super();
  factory GetAnalysisResponse() => create();
  factory GetAnalysisResponse.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory GetAnalysisResponse.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  GetAnalysisResponse clone() => GetAnalysisResponse()..mergeFromMessage(this);
  GetAnalysisResponse copyWith(void Function(GetAnalysisResponse) updates) => super.copyWith((message) => updates(message as GetAnalysisResponse));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static GetAnalysisResponse create() => GetAnalysisResponse._();
  GetAnalysisResponse createEmptyInstance() => create();
  static $pb.PbList<GetAnalysisResponse> createRepeated() => $pb.PbList<GetAnalysisResponse>();
  @$core.pragma('dart2js:noInline')
  static GetAnalysisResponse getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<GetAnalysisResponse>(create);
  static GetAnalysisResponse _defaultInstance;

  @$pb.TagNumber(1)
  $core.String get graphId => $_getSZ(0);
  @$pb.TagNumber(1)
  set graphId($core.String v) { $_setString(0, v); }
  @$pb.TagNumber(1)
  $core.bool hasGraphId() => $_has(0);
  @$pb.TagNumber(1)
  void clearGraphId() => clearField(1);

  @$pb.TagNumber(101)
  $core.List<$1.Token_Word> get vocabulary => $_getList(1);

  @$pb.TagNumber(102)
  $core.List<GetAnalysisResponse_WordFrequency> get wordFrequencies => $_getList(2);
}

class GenerateResponseRequest extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('GenerateResponseRequest', package: const $pb.PackageName('org.xanho.proto.service.knowledgegraph'), createEmptyInstance: create)
    ..aOS(1, 'graphId', protoName: 'graphId')
    ..hasRequiredFields = false
  ;

  GenerateResponseRequest._() : super();
  factory GenerateResponseRequest() => create();
  factory GenerateResponseRequest.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory GenerateResponseRequest.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  GenerateResponseRequest clone() => GenerateResponseRequest()..mergeFromMessage(this);
  GenerateResponseRequest copyWith(void Function(GenerateResponseRequest) updates) => super.copyWith((message) => updates(message as GenerateResponseRequest));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static GenerateResponseRequest create() => GenerateResponseRequest._();
  GenerateResponseRequest createEmptyInstance() => create();
  static $pb.PbList<GenerateResponseRequest> createRepeated() => $pb.PbList<GenerateResponseRequest>();
  @$core.pragma('dart2js:noInline')
  static GenerateResponseRequest getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<GenerateResponseRequest>(create);
  static GenerateResponseRequest _defaultInstance;

  @$pb.TagNumber(1)
  $core.String get graphId => $_getSZ(0);
  @$pb.TagNumber(1)
  set graphId($core.String v) { $_setString(0, v); }
  @$pb.TagNumber(1)
  $core.bool hasGraphId() => $_has(0);
  @$pb.TagNumber(1)
  void clearGraphId() => clearField(1);
}

class GenerateResponseResponse extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('GenerateResponseResponse', package: const $pb.PackageName('org.xanho.proto.service.knowledgegraph'), createEmptyInstance: create)
    ..aOS(1, 'graphId', protoName: 'graphId')
    ..aOM<$1.Document>(2, 'document', subBuilder: $1.Document.create)
    ..hasRequiredFields = false
  ;

  GenerateResponseResponse._() : super();
  factory GenerateResponseResponse() => create();
  factory GenerateResponseResponse.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory GenerateResponseResponse.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  GenerateResponseResponse clone() => GenerateResponseResponse()..mergeFromMessage(this);
  GenerateResponseResponse copyWith(void Function(GenerateResponseResponse) updates) => super.copyWith((message) => updates(message as GenerateResponseResponse));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static GenerateResponseResponse create() => GenerateResponseResponse._();
  GenerateResponseResponse createEmptyInstance() => create();
  static $pb.PbList<GenerateResponseResponse> createRepeated() => $pb.PbList<GenerateResponseResponse>();
  @$core.pragma('dart2js:noInline')
  static GenerateResponseResponse getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<GenerateResponseResponse>(create);
  static GenerateResponseResponse _defaultInstance;

  @$pb.TagNumber(1)
  $core.String get graphId => $_getSZ(0);
  @$pb.TagNumber(1)
  set graphId($core.String v) { $_setString(0, v); }
  @$pb.TagNumber(1)
  $core.bool hasGraphId() => $_has(0);
  @$pb.TagNumber(1)
  void clearGraphId() => clearField(1);

  @$pb.TagNumber(2)
  $1.Document get document => $_getN(1);
  @$pb.TagNumber(2)
  set document($1.Document v) { setField(2, v); }
  @$pb.TagNumber(2)
  $core.bool hasDocument() => $_has(1);
  @$pb.TagNumber(2)
  void clearDocument() => clearField(2);
  @$pb.TagNumber(2)
  $1.Document ensureDocument() => $_ensure(1);
}


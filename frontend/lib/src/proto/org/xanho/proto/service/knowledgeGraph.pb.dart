///
//  Generated code. Do not modify.
//  source: org/xanho/proto/service/knowledgeGraph.proto
//
// @dart = 2.3
// ignore_for_file: camel_case_types,non_constant_identifier_names,library_prefixes,unused_import,unused_shown_name,return_of_invalid_type

import 'dart:core' as $core;

import 'package:fixnum/fixnum.dart' as $fixnum;
import 'package:protobuf/protobuf.dart' as $pb;

import 'knowledgeGraph.pbenum.dart';

export 'knowledgeGraph.pbenum.dart';

class MessagesStreamRequest extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('MessagesStreamRequest', package: const $pb.PackageName('org.xanho.proto.service.knowledgegraph'), createEmptyInstance: create)
    ..aOS(1, 'graphId', protoName: 'graphId')
    ..hasRequiredFields = false
  ;

  MessagesStreamRequest._() : super();
  factory MessagesStreamRequest() => create();
  factory MessagesStreamRequest.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory MessagesStreamRequest.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  MessagesStreamRequest clone() => MessagesStreamRequest()..mergeFromMessage(this);
  MessagesStreamRequest copyWith(void Function(MessagesStreamRequest) updates) => super.copyWith((message) => updates(message as MessagesStreamRequest));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static MessagesStreamRequest create() => MessagesStreamRequest._();
  MessagesStreamRequest createEmptyInstance() => create();
  static $pb.PbList<MessagesStreamRequest> createRepeated() => $pb.PbList<MessagesStreamRequest>();
  @$core.pragma('dart2js:noInline')
  static MessagesStreamRequest getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<MessagesStreamRequest>(create);
  static MessagesStreamRequest _defaultInstance;

  @$pb.TagNumber(1)
  $core.String get graphId => $_getSZ(0);
  @$pb.TagNumber(1)
  set graphId($core.String v) { $_setString(0, v); }
  @$pb.TagNumber(1)
  $core.bool hasGraphId() => $_has(0);
  @$pb.TagNumber(1)
  void clearGraphId() => clearField(1);
}

class TextMessage extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('TextMessage', package: const $pb.PackageName('org.xanho.proto.service.knowledgegraph'), createEmptyInstance: create)
    ..aOS(1, 'id')
    ..e<MessageSource>(2, 'source', $pb.PbFieldType.OE, defaultOrMaker: MessageSource.USER, valueOf: MessageSource.valueOf, enumValues: MessageSource.values)
    ..aInt64(3, 'timestampMs', protoName: 'timestampMs')
    ..aOS(4, 'text')
    ..hasRequiredFields = false
  ;

  TextMessage._() : super();
  factory TextMessage() => create();
  factory TextMessage.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory TextMessage.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  TextMessage clone() => TextMessage()..mergeFromMessage(this);
  TextMessage copyWith(void Function(TextMessage) updates) => super.copyWith((message) => updates(message as TextMessage));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static TextMessage create() => TextMessage._();
  TextMessage createEmptyInstance() => create();
  static $pb.PbList<TextMessage> createRepeated() => $pb.PbList<TextMessage>();
  @$core.pragma('dart2js:noInline')
  static TextMessage getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<TextMessage>(create);
  static TextMessage _defaultInstance;

  @$pb.TagNumber(1)
  $core.String get id => $_getSZ(0);
  @$pb.TagNumber(1)
  set id($core.String v) { $_setString(0, v); }
  @$pb.TagNumber(1)
  $core.bool hasId() => $_has(0);
  @$pb.TagNumber(1)
  void clearId() => clearField(1);

  @$pb.TagNumber(2)
  MessageSource get source => $_getN(1);
  @$pb.TagNumber(2)
  set source(MessageSource v) { setField(2, v); }
  @$pb.TagNumber(2)
  $core.bool hasSource() => $_has(1);
  @$pb.TagNumber(2)
  void clearSource() => clearField(2);

  @$pb.TagNumber(3)
  $fixnum.Int64 get timestampMs => $_getI64(2);
  @$pb.TagNumber(3)
  set timestampMs($fixnum.Int64 v) { $_setInt64(2, v); }
  @$pb.TagNumber(3)
  $core.bool hasTimestampMs() => $_has(2);
  @$pb.TagNumber(3)
  void clearTimestampMs() => clearField(3);

  @$pb.TagNumber(4)
  $core.String get text => $_getSZ(3);
  @$pb.TagNumber(4)
  set text($core.String v) { $_setString(3, v); }
  @$pb.TagNumber(4)
  $core.bool hasText() => $_has(3);
  @$pb.TagNumber(4)
  void clearText() => clearField(4);
}

class SendTextMessageRequest extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('SendTextMessageRequest', package: const $pb.PackageName('org.xanho.proto.service.knowledgegraph'), createEmptyInstance: create)
    ..aOS(1, 'graphId', protoName: 'graphId')
    ..aOM<TextMessage>(2, 'message', subBuilder: TextMessage.create)
    ..hasRequiredFields = false
  ;

  SendTextMessageRequest._() : super();
  factory SendTextMessageRequest() => create();
  factory SendTextMessageRequest.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory SendTextMessageRequest.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  SendTextMessageRequest clone() => SendTextMessageRequest()..mergeFromMessage(this);
  SendTextMessageRequest copyWith(void Function(SendTextMessageRequest) updates) => super.copyWith((message) => updates(message as SendTextMessageRequest));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static SendTextMessageRequest create() => SendTextMessageRequest._();
  SendTextMessageRequest createEmptyInstance() => create();
  static $pb.PbList<SendTextMessageRequest> createRepeated() => $pb.PbList<SendTextMessageRequest>();
  @$core.pragma('dart2js:noInline')
  static SendTextMessageRequest getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<SendTextMessageRequest>(create);
  static SendTextMessageRequest _defaultInstance;

  @$pb.TagNumber(1)
  $core.String get graphId => $_getSZ(0);
  @$pb.TagNumber(1)
  set graphId($core.String v) { $_setString(0, v); }
  @$pb.TagNumber(1)
  $core.bool hasGraphId() => $_has(0);
  @$pb.TagNumber(1)
  void clearGraphId() => clearField(1);

  @$pb.TagNumber(2)
  TextMessage get message => $_getN(1);
  @$pb.TagNumber(2)
  set message(TextMessage v) { setField(2, v); }
  @$pb.TagNumber(2)
  $core.bool hasMessage() => $_has(1);
  @$pb.TagNumber(2)
  void clearMessage() => clearField(2);
  @$pb.TagNumber(2)
  TextMessage ensureMessage() => $_ensure(1);
}

class SendTextMessageResponse extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('SendTextMessageResponse', package: const $pb.PackageName('org.xanho.proto.service.knowledgegraph'), createEmptyInstance: create)
    ..aOS(1, 'graphId', protoName: 'graphId')
    ..aOB(2, 'success')
    ..hasRequiredFields = false
  ;

  SendTextMessageResponse._() : super();
  factory SendTextMessageResponse() => create();
  factory SendTextMessageResponse.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory SendTextMessageResponse.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  SendTextMessageResponse clone() => SendTextMessageResponse()..mergeFromMessage(this);
  SendTextMessageResponse copyWith(void Function(SendTextMessageResponse) updates) => super.copyWith((message) => updates(message as SendTextMessageResponse));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static SendTextMessageResponse create() => SendTextMessageResponse._();
  SendTextMessageResponse createEmptyInstance() => create();
  static $pb.PbList<SendTextMessageResponse> createRepeated() => $pb.PbList<SendTextMessageResponse>();
  @$core.pragma('dart2js:noInline')
  static SendTextMessageResponse getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<SendTextMessageResponse>(create);
  static SendTextMessageResponse _defaultInstance;

  @$pb.TagNumber(1)
  $core.String get graphId => $_getSZ(0);
  @$pb.TagNumber(1)
  set graphId($core.String v) { $_setString(0, v); }
  @$pb.TagNumber(1)
  $core.bool hasGraphId() => $_has(0);
  @$pb.TagNumber(1)
  void clearGraphId() => clearField(1);

  @$pb.TagNumber(2)
  $core.bool get success => $_getBF(1);
  @$pb.TagNumber(2)
  set success($core.bool v) { $_setBool(1, v); }
  @$pb.TagNumber(2)
  $core.bool hasSuccess() => $_has(1);
  @$pb.TagNumber(2)
  void clearSuccess() => clearField(2);
}


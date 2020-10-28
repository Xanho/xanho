///
//  Generated code. Do not modify.
//  source: org/xanho/proto/service/knowledgeGraph.proto
//
// @dart = 2.3
// ignore_for_file: camel_case_types,non_constant_identifier_names,library_prefixes,unused_import,unused_shown_name,return_of_invalid_type

import 'dart:async' as $async;

import 'dart:core' as $core;

import 'package:grpc/service_api.dart' as $grpc;
import 'knowledgeGraph.pb.dart' as $0;
export 'knowledgeGraph.pb.dart';

class KnowledgeGraphServiceClient extends $grpc.Client {
  static final _$messagesStream = $grpc.ClientMethod<$0.MessagesStreamRequest,
          $0.TextMessage>(
      '/org.xanho.proto.service.knowledgegraph.KnowledgeGraphService/MessagesStream',
      ($0.MessagesStreamRequest value) => value.writeToBuffer(),
      ($core.List<$core.int> value) => $0.TextMessage.fromBuffer(value));
  static final _$sendTextMessage = $grpc.ClientMethod<$0.SendTextMessageRequest,
          $0.SendTextMessageResponse>(
      '/org.xanho.proto.service.knowledgegraph.KnowledgeGraphService/SendTextMessage',
      ($0.SendTextMessageRequest value) => value.writeToBuffer(),
      ($core.List<$core.int> value) =>
          $0.SendTextMessageResponse.fromBuffer(value));
  static final _$getGraph = $grpc.ClientMethod<$0.GetGraphRequest,
          $0.GetGraphResponse>(
      '/org.xanho.proto.service.knowledgegraph.KnowledgeGraphService/GetGraph',
      ($0.GetGraphRequest value) => value.writeToBuffer(),
      ($core.List<$core.int> value) => $0.GetGraphResponse.fromBuffer(value));

  KnowledgeGraphServiceClient($grpc.ClientChannel channel,
      {$grpc.CallOptions options})
      : super(channel, options: options);

  $grpc.ResponseStream<$0.TextMessage> messagesStream(
      $0.MessagesStreamRequest request,
      {$grpc.CallOptions options}) {
    final call = $createCall(
        _$messagesStream, $async.Stream.fromIterable([request]),
        options: options);
    return $grpc.ResponseStream(call);
  }

  $grpc.ResponseFuture<$0.SendTextMessageResponse> sendTextMessage(
      $0.SendTextMessageRequest request,
      {$grpc.CallOptions options}) {
    final call = $createCall(
        _$sendTextMessage, $async.Stream.fromIterable([request]),
        options: options);
    return $grpc.ResponseFuture(call);
  }

  $grpc.ResponseFuture<$0.GetGraphResponse> getGraph($0.GetGraphRequest request,
      {$grpc.CallOptions options}) {
    final call = $createCall(_$getGraph, $async.Stream.fromIterable([request]),
        options: options);
    return $grpc.ResponseFuture(call);
  }
}

abstract class KnowledgeGraphServiceBase extends $grpc.Service {
  $core.String get $name =>
      'org.xanho.proto.service.knowledgegraph.KnowledgeGraphService';

  KnowledgeGraphServiceBase() {
    $addMethod($grpc.ServiceMethod<$0.MessagesStreamRequest, $0.TextMessage>(
        'MessagesStream',
        messagesStream_Pre,
        false,
        true,
        ($core.List<$core.int> value) =>
            $0.MessagesStreamRequest.fromBuffer(value),
        ($0.TextMessage value) => value.writeToBuffer()));
    $addMethod($grpc.ServiceMethod<$0.SendTextMessageRequest,
            $0.SendTextMessageResponse>(
        'SendTextMessage',
        sendTextMessage_Pre,
        false,
        false,
        ($core.List<$core.int> value) =>
            $0.SendTextMessageRequest.fromBuffer(value),
        ($0.SendTextMessageResponse value) => value.writeToBuffer()));
    $addMethod($grpc.ServiceMethod<$0.GetGraphRequest, $0.GetGraphResponse>(
        'GetGraph',
        getGraph_Pre,
        false,
        false,
        ($core.List<$core.int> value) => $0.GetGraphRequest.fromBuffer(value),
        ($0.GetGraphResponse value) => value.writeToBuffer()));
  }

  $async.Stream<$0.TextMessage> messagesStream_Pre($grpc.ServiceCall call,
      $async.Future<$0.MessagesStreamRequest> request) async* {
    yield* messagesStream(call, await request);
  }

  $async.Future<$0.SendTextMessageResponse> sendTextMessage_Pre(
      $grpc.ServiceCall call,
      $async.Future<$0.SendTextMessageRequest> request) async {
    return sendTextMessage(call, await request);
  }

  $async.Future<$0.GetGraphResponse> getGraph_Pre(
      $grpc.ServiceCall call, $async.Future<$0.GetGraphRequest> request) async {
    return getGraph(call, await request);
  }

  $async.Stream<$0.TextMessage> messagesStream(
      $grpc.ServiceCall call, $0.MessagesStreamRequest request);
  $async.Future<$0.SendTextMessageResponse> sendTextMessage(
      $grpc.ServiceCall call, $0.SendTextMessageRequest request);
  $async.Future<$0.GetGraphResponse> getGraph(
      $grpc.ServiceCall call, $0.GetGraphRequest request);
}

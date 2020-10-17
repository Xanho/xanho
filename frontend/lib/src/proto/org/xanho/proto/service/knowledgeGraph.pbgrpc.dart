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
  static final _$ingestTextStream = $grpc.ClientMethod<$0.IngestTextRequest,
          $0.IngestTextStreamResponse>(
      '/org.xanho.proto.service.knowledgegraph.KnowledgeGraphService/IngestTextStream',
      ($0.IngestTextRequest value) => value.writeToBuffer(),
      ($core.List<$core.int> value) =>
          $0.IngestTextStreamResponse.fromBuffer(value));
  static final _$getAnalysis = $grpc.ClientMethod<$0.GetAnalysisRequest,
          $0.GetAnalysisResponse>(
      '/org.xanho.proto.service.knowledgegraph.KnowledgeGraphService/GetAnalysis',
      ($0.GetAnalysisRequest value) => value.writeToBuffer(),
      ($core.List<$core.int> value) =>
          $0.GetAnalysisResponse.fromBuffer(value));
  static final _$generateResponse = $grpc.ClientMethod<
          $0.GenerateResponseRequest, $0.GenerateResponseResponse>(
      '/org.xanho.proto.service.knowledgegraph.KnowledgeGraphService/GenerateResponse',
      ($0.GenerateResponseRequest value) => value.writeToBuffer(),
      ($core.List<$core.int> value) =>
          $0.GenerateResponseResponse.fromBuffer(value));

  KnowledgeGraphServiceClient($grpc.ClientChannel channel,
      {$grpc.CallOptions options})
      : super(channel, options: options);

  $grpc.ResponseFuture<$0.IngestTextStreamResponse> ingestTextStream(
      $async.Stream<$0.IngestTextRequest> request,
      {$grpc.CallOptions options}) {
    final call = $createCall(_$ingestTextStream, request, options: options);
    return $grpc.ResponseFuture(call);
  }

  $grpc.ResponseFuture<$0.GetAnalysisResponse> getAnalysis(
      $0.GetAnalysisRequest request,
      {$grpc.CallOptions options}) {
    final call = $createCall(
        _$getAnalysis, $async.Stream.fromIterable([request]),
        options: options);
    return $grpc.ResponseFuture(call);
  }

  $grpc.ResponseFuture<$0.GenerateResponseResponse> generateResponse(
      $0.GenerateResponseRequest request,
      {$grpc.CallOptions options}) {
    final call = $createCall(
        _$generateResponse, $async.Stream.fromIterable([request]),
        options: options);
    return $grpc.ResponseFuture(call);
  }
}

abstract class KnowledgeGraphServiceBase extends $grpc.Service {
  $core.String get $name =>
      'org.xanho.proto.service.knowledgegraph.KnowledgeGraphService';

  KnowledgeGraphServiceBase() {
    $addMethod(
        $grpc.ServiceMethod<$0.IngestTextRequest, $0.IngestTextStreamResponse>(
            'IngestTextStream',
            ingestTextStream,
            true,
            false,
            ($core.List<$core.int> value) =>
                $0.IngestTextRequest.fromBuffer(value),
            ($0.IngestTextStreamResponse value) => value.writeToBuffer()));
    $addMethod(
        $grpc.ServiceMethod<$0.GetAnalysisRequest, $0.GetAnalysisResponse>(
            'GetAnalysis',
            getAnalysis_Pre,
            false,
            false,
            ($core.List<$core.int> value) =>
                $0.GetAnalysisRequest.fromBuffer(value),
            ($0.GetAnalysisResponse value) => value.writeToBuffer()));
    $addMethod($grpc.ServiceMethod<$0.GenerateResponseRequest,
            $0.GenerateResponseResponse>(
        'GenerateResponse',
        generateResponse_Pre,
        false,
        false,
        ($core.List<$core.int> value) =>
            $0.GenerateResponseRequest.fromBuffer(value),
        ($0.GenerateResponseResponse value) => value.writeToBuffer()));
  }

  $async.Future<$0.GetAnalysisResponse> getAnalysis_Pre($grpc.ServiceCall call,
      $async.Future<$0.GetAnalysisRequest> request) async {
    return getAnalysis(call, await request);
  }

  $async.Future<$0.GenerateResponseResponse> generateResponse_Pre(
      $grpc.ServiceCall call,
      $async.Future<$0.GenerateResponseRequest> request) async {
    return generateResponse(call, await request);
  }

  $async.Future<$0.IngestTextStreamResponse> ingestTextStream(
      $grpc.ServiceCall call, $async.Stream<$0.IngestTextRequest> request);
  $async.Future<$0.GetAnalysisResponse> getAnalysis(
      $grpc.ServiceCall call, $0.GetAnalysisRequest request);
  $async.Future<$0.GenerateResponseResponse> generateResponse(
      $grpc.ServiceCall call, $0.GenerateResponseRequest request);
}

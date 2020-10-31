import 'package:grpc/grpc_connection_interface.dart';
import 'package:grpc/grpc_web.dart';
import 'package:frontend/src/settings/config.prod.dart' as config;

ClientChannelBase channel = GrpcWebClientChannel.xhr(
  Uri.parse(
      "${config.GRPC_SERVICE_PROTOCOL}://${config.GRPC_SERVICE_HOST}:${config.GRPC_SERVICE_PORT}"),
);

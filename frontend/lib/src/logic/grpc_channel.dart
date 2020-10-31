import 'package:grpc/grpc.dart';
import 'package:grpc/grpc_connection_interface.dart';
import 'package:frontend/src/settings/config.dart' as config;

ClientChannelBase channel = ClientChannel(
  config.GRPC_SERVICE_HOST,
  port: config.GRPC_SERVICE_PORT,
);

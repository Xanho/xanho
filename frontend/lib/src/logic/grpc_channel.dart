import 'package:grpc/grpc.dart';
import 'package:grpc/grpc_connection_interface.dart';

ClientChannelBase channel = ClientChannel(
  'backend.xanho.org',
  port: 443,
);

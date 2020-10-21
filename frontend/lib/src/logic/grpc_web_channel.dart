import 'package:grpc/grpc_connection_interface.dart';
import 'package:grpc/grpc_web.dart';

ClientChannelBase channel = GrpcWebClientChannel.xhr(
  // Uri.parse("https://backend.xanho.org:443"),
  Uri.parse("http://localhost:8447"),
);

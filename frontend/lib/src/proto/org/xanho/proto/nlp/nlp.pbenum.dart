///
//  Generated code. Do not modify.
//  source: org/xanho/proto/nlp/nlp.proto
//
// @dart = 2.3
// ignore_for_file: camel_case_types,non_constant_identifier_names,library_prefixes,unused_import,unused_shown_name,return_of_invalid_type

// ignore_for_file: UNDEFINED_SHOWN_NAME,UNUSED_SHOWN_NAME
import 'dart:core' as $core;
import 'package:protobuf/protobuf.dart' as $pb;

class Sentence_SentenceType extends $pb.ProtobufEnum {
  static const Sentence_SentenceType STATEMENT = Sentence_SentenceType._(0, 'STATEMENT');
  static const Sentence_SentenceType QUESTION = Sentence_SentenceType._(1, 'QUESTION');

  static const $core.List<Sentence_SentenceType> values = <Sentence_SentenceType> [
    STATEMENT,
    QUESTION,
  ];

  static final $core.Map<$core.int, Sentence_SentenceType> _byValue = $pb.ProtobufEnum.initByValue(values);
  static Sentence_SentenceType valueOf($core.int value) => _byValue[value];

  const Sentence_SentenceType._($core.int v, $core.String n) : super(v, n);
}


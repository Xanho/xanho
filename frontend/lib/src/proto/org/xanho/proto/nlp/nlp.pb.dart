///
//  Generated code. Do not modify.
//  source: org/xanho/proto/nlp/nlp.proto
//
// @dart = 2.3
// ignore_for_file: camel_case_types,non_constant_identifier_names,library_prefixes,unused_import,unused_shown_name,return_of_invalid_type

import 'dart:core' as $core;

import 'package:protobuf/protobuf.dart' as $pb;

import 'nlp.pbenum.dart';

export 'nlp.pbenum.dart';

class Text extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('Text', package: const $pb.PackageName('org.xanho.proto.nlp'), createEmptyInstance: create)
    ..aOS(1, 'text')
    ..hasRequiredFields = false
  ;

  Text._() : super();
  factory Text() => create();
  factory Text.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory Text.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  Text clone() => Text()..mergeFromMessage(this);
  Text copyWith(void Function(Text) updates) => super.copyWith((message) => updates(message as Text));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static Text create() => Text._();
  Text createEmptyInstance() => create();
  static $pb.PbList<Text> createRepeated() => $pb.PbList<Text>();
  @$core.pragma('dart2js:noInline')
  static Text getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<Text>(create);
  static Text _defaultInstance;

  @$pb.TagNumber(1)
  $core.String get text => $_getSZ(0);
  @$pb.TagNumber(1)
  set text($core.String v) { $_setString(0, v); }
  @$pb.TagNumber(1)
  $core.bool hasText() => $_has(0);
  @$pb.TagNumber(1)
  void clearText() => clearField(1);
}

class Token_Space extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('Token.Space', package: const $pb.PackageName('org.xanho.proto.nlp'), createEmptyInstance: create)
    ..hasRequiredFields = false
  ;

  Token_Space._() : super();
  factory Token_Space() => create();
  factory Token_Space.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory Token_Space.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  Token_Space clone() => Token_Space()..mergeFromMessage(this);
  Token_Space copyWith(void Function(Token_Space) updates) => super.copyWith((message) => updates(message as Token_Space));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static Token_Space create() => Token_Space._();
  Token_Space createEmptyInstance() => create();
  static $pb.PbList<Token_Space> createRepeated() => $pb.PbList<Token_Space>();
  @$core.pragma('dart2js:noInline')
  static Token_Space getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<Token_Space>(create);
  static Token_Space _defaultInstance;
}

class Token_LineBreak extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('Token.LineBreak', package: const $pb.PackageName('org.xanho.proto.nlp'), createEmptyInstance: create)
    ..hasRequiredFields = false
  ;

  Token_LineBreak._() : super();
  factory Token_LineBreak() => create();
  factory Token_LineBreak.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory Token_LineBreak.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  Token_LineBreak clone() => Token_LineBreak()..mergeFromMessage(this);
  Token_LineBreak copyWith(void Function(Token_LineBreak) updates) => super.copyWith((message) => updates(message as Token_LineBreak));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static Token_LineBreak create() => Token_LineBreak._();
  Token_LineBreak createEmptyInstance() => create();
  static $pb.PbList<Token_LineBreak> createRepeated() => $pb.PbList<Token_LineBreak>();
  @$core.pragma('dart2js:noInline')
  static Token_LineBreak getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<Token_LineBreak>(create);
  static Token_LineBreak _defaultInstance;
}

class Token_Punctuation extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('Token.Punctuation', package: const $pb.PackageName('org.xanho.proto.nlp'), createEmptyInstance: create)
    ..aOS(1, 'value')
    ..hasRequiredFields = false
  ;

  Token_Punctuation._() : super();
  factory Token_Punctuation() => create();
  factory Token_Punctuation.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory Token_Punctuation.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  Token_Punctuation clone() => Token_Punctuation()..mergeFromMessage(this);
  Token_Punctuation copyWith(void Function(Token_Punctuation) updates) => super.copyWith((message) => updates(message as Token_Punctuation));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static Token_Punctuation create() => Token_Punctuation._();
  Token_Punctuation createEmptyInstance() => create();
  static $pb.PbList<Token_Punctuation> createRepeated() => $pb.PbList<Token_Punctuation>();
  @$core.pragma('dart2js:noInline')
  static Token_Punctuation getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<Token_Punctuation>(create);
  static Token_Punctuation _defaultInstance;

  @$pb.TagNumber(1)
  $core.String get value => $_getSZ(0);
  @$pb.TagNumber(1)
  set value($core.String v) { $_setString(0, v); }
  @$pb.TagNumber(1)
  $core.bool hasValue() => $_has(0);
  @$pb.TagNumber(1)
  void clearValue() => clearField(1);
}

class Token_Word extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('Token.Word', package: const $pb.PackageName('org.xanho.proto.nlp'), createEmptyInstance: create)
    ..aOS(1, 'value')
    ..hasRequiredFields = false
  ;

  Token_Word._() : super();
  factory Token_Word() => create();
  factory Token_Word.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory Token_Word.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  Token_Word clone() => Token_Word()..mergeFromMessage(this);
  Token_Word copyWith(void Function(Token_Word) updates) => super.copyWith((message) => updates(message as Token_Word));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static Token_Word create() => Token_Word._();
  Token_Word createEmptyInstance() => create();
  static $pb.PbList<Token_Word> createRepeated() => $pb.PbList<Token_Word>();
  @$core.pragma('dart2js:noInline')
  static Token_Word getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<Token_Word>(create);
  static Token_Word _defaultInstance;

  @$pb.TagNumber(1)
  $core.String get value => $_getSZ(0);
  @$pb.TagNumber(1)
  set value($core.String v) { $_setString(0, v); }
  @$pb.TagNumber(1)
  $core.bool hasValue() => $_has(0);
  @$pb.TagNumber(1)
  void clearValue() => clearField(1);
}

enum Token_Value {
  space, 
  lineBreak, 
  punctuation, 
  word, 
  notSet
}

class Token extends $pb.GeneratedMessage {
  static const $core.Map<$core.int, Token_Value> _Token_ValueByTag = {
    1 : Token_Value.space,
    2 : Token_Value.lineBreak,
    3 : Token_Value.punctuation,
    4 : Token_Value.word,
    0 : Token_Value.notSet
  };
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('Token', package: const $pb.PackageName('org.xanho.proto.nlp'), createEmptyInstance: create)
    ..oo(0, [1, 2, 3, 4])
    ..aOM<Token_Space>(1, 'space', subBuilder: Token_Space.create)
    ..aOM<Token_LineBreak>(2, 'lineBreak', protoName: 'lineBreak', subBuilder: Token_LineBreak.create)
    ..aOM<Token_Punctuation>(3, 'punctuation', subBuilder: Token_Punctuation.create)
    ..aOM<Token_Word>(4, 'word', subBuilder: Token_Word.create)
    ..hasRequiredFields = false
  ;

  Token._() : super();
  factory Token() => create();
  factory Token.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory Token.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  Token clone() => Token()..mergeFromMessage(this);
  Token copyWith(void Function(Token) updates) => super.copyWith((message) => updates(message as Token));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static Token create() => Token._();
  Token createEmptyInstance() => create();
  static $pb.PbList<Token> createRepeated() => $pb.PbList<Token>();
  @$core.pragma('dart2js:noInline')
  static Token getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<Token>(create);
  static Token _defaultInstance;

  Token_Value whichValue() => _Token_ValueByTag[$_whichOneof(0)];
  void clearValue() => clearField($_whichOneof(0));

  @$pb.TagNumber(1)
  Token_Space get space => $_getN(0);
  @$pb.TagNumber(1)
  set space(Token_Space v) { setField(1, v); }
  @$pb.TagNumber(1)
  $core.bool hasSpace() => $_has(0);
  @$pb.TagNumber(1)
  void clearSpace() => clearField(1);
  @$pb.TagNumber(1)
  Token_Space ensureSpace() => $_ensure(0);

  @$pb.TagNumber(2)
  Token_LineBreak get lineBreak => $_getN(1);
  @$pb.TagNumber(2)
  set lineBreak(Token_LineBreak v) { setField(2, v); }
  @$pb.TagNumber(2)
  $core.bool hasLineBreak() => $_has(1);
  @$pb.TagNumber(2)
  void clearLineBreak() => clearField(2);
  @$pb.TagNumber(2)
  Token_LineBreak ensureLineBreak() => $_ensure(1);

  @$pb.TagNumber(3)
  Token_Punctuation get punctuation => $_getN(2);
  @$pb.TagNumber(3)
  set punctuation(Token_Punctuation v) { setField(3, v); }
  @$pb.TagNumber(3)
  $core.bool hasPunctuation() => $_has(2);
  @$pb.TagNumber(3)
  void clearPunctuation() => clearField(3);
  @$pb.TagNumber(3)
  Token_Punctuation ensurePunctuation() => $_ensure(2);

  @$pb.TagNumber(4)
  Token_Word get word => $_getN(3);
  @$pb.TagNumber(4)
  set word(Token_Word v) { setField(4, v); }
  @$pb.TagNumber(4)
  $core.bool hasWord() => $_has(3);
  @$pb.TagNumber(4)
  void clearWord() => clearField(4);
  @$pb.TagNumber(4)
  Token_Word ensureWord() => $_ensure(3);
}

class Phrase extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('Phrase', package: const $pb.PackageName('org.xanho.proto.nlp'), createEmptyInstance: create)
    ..pc<Token_Word>(1, 'words', $pb.PbFieldType.PM, subBuilder: Token_Word.create)
    ..hasRequiredFields = false
  ;

  Phrase._() : super();
  factory Phrase() => create();
  factory Phrase.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory Phrase.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  Phrase clone() => Phrase()..mergeFromMessage(this);
  Phrase copyWith(void Function(Phrase) updates) => super.copyWith((message) => updates(message as Phrase));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static Phrase create() => Phrase._();
  Phrase createEmptyInstance() => create();
  static $pb.PbList<Phrase> createRepeated() => $pb.PbList<Phrase>();
  @$core.pragma('dart2js:noInline')
  static Phrase getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<Phrase>(create);
  static Phrase _defaultInstance;

  @$pb.TagNumber(1)
  $core.List<Token_Word> get words => $_getList(0);
}

class Sentence extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('Sentence', package: const $pb.PackageName('org.xanho.proto.nlp'), createEmptyInstance: create)
    ..aOM<Phrase>(1, 'phrase', subBuilder: Phrase.create)
    ..aOM<Token_Punctuation>(2, 'punctuation', subBuilder: Token_Punctuation.create)
    ..e<Sentence_SentenceType>(3, 'sentenceType', $pb.PbFieldType.OE, protoName: 'sentenceType', defaultOrMaker: Sentence_SentenceType.STATEMENT, valueOf: Sentence_SentenceType.valueOf, enumValues: Sentence_SentenceType.values)
    ..hasRequiredFields = false
  ;

  Sentence._() : super();
  factory Sentence() => create();
  factory Sentence.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory Sentence.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  Sentence clone() => Sentence()..mergeFromMessage(this);
  Sentence copyWith(void Function(Sentence) updates) => super.copyWith((message) => updates(message as Sentence));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static Sentence create() => Sentence._();
  Sentence createEmptyInstance() => create();
  static $pb.PbList<Sentence> createRepeated() => $pb.PbList<Sentence>();
  @$core.pragma('dart2js:noInline')
  static Sentence getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<Sentence>(create);
  static Sentence _defaultInstance;

  @$pb.TagNumber(1)
  Phrase get phrase => $_getN(0);
  @$pb.TagNumber(1)
  set phrase(Phrase v) { setField(1, v); }
  @$pb.TagNumber(1)
  $core.bool hasPhrase() => $_has(0);
  @$pb.TagNumber(1)
  void clearPhrase() => clearField(1);
  @$pb.TagNumber(1)
  Phrase ensurePhrase() => $_ensure(0);

  @$pb.TagNumber(2)
  Token_Punctuation get punctuation => $_getN(1);
  @$pb.TagNumber(2)
  set punctuation(Token_Punctuation v) { setField(2, v); }
  @$pb.TagNumber(2)
  $core.bool hasPunctuation() => $_has(1);
  @$pb.TagNumber(2)
  void clearPunctuation() => clearField(2);
  @$pb.TagNumber(2)
  Token_Punctuation ensurePunctuation() => $_ensure(1);

  @$pb.TagNumber(3)
  Sentence_SentenceType get sentenceType => $_getN(2);
  @$pb.TagNumber(3)
  set sentenceType(Sentence_SentenceType v) { setField(3, v); }
  @$pb.TagNumber(3)
  $core.bool hasSentenceType() => $_has(2);
  @$pb.TagNumber(3)
  void clearSentenceType() => clearField(3);
}

class Paragraph extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('Paragraph', package: const $pb.PackageName('org.xanho.proto.nlp'), createEmptyInstance: create)
    ..pc<Sentence>(1, 'sentences', $pb.PbFieldType.PM, subBuilder: Sentence.create)
    ..hasRequiredFields = false
  ;

  Paragraph._() : super();
  factory Paragraph() => create();
  factory Paragraph.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory Paragraph.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  Paragraph clone() => Paragraph()..mergeFromMessage(this);
  Paragraph copyWith(void Function(Paragraph) updates) => super.copyWith((message) => updates(message as Paragraph));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static Paragraph create() => Paragraph._();
  Paragraph createEmptyInstance() => create();
  static $pb.PbList<Paragraph> createRepeated() => $pb.PbList<Paragraph>();
  @$core.pragma('dart2js:noInline')
  static Paragraph getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<Paragraph>(create);
  static Paragraph _defaultInstance;

  @$pb.TagNumber(1)
  $core.List<Sentence> get sentences => $_getList(0);
}

class Document extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('Document', package: const $pb.PackageName('org.xanho.proto.nlp'), createEmptyInstance: create)
    ..pc<Paragraph>(1, 'paragraphs', $pb.PbFieldType.PM, subBuilder: Paragraph.create)
    ..hasRequiredFields = false
  ;

  Document._() : super();
  factory Document() => create();
  factory Document.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory Document.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  Document clone() => Document()..mergeFromMessage(this);
  Document copyWith(void Function(Document) updates) => super.copyWith((message) => updates(message as Document));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static Document create() => Document._();
  Document createEmptyInstance() => create();
  static $pb.PbList<Document> createRepeated() => $pb.PbList<Document>();
  @$core.pragma('dart2js:noInline')
  static Document getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<Document>(create);
  static Document _defaultInstance;

  @$pb.TagNumber(1)
  $core.List<Paragraph> get paragraphs => $_getList(0);
}

class ParseResult extends $pb.GeneratedMessage {
  static final $pb.BuilderInfo _i = $pb.BuilderInfo('ParseResult', package: const $pb.PackageName('org.xanho.proto.nlp'), createEmptyInstance: create)
    ..aOM<Text>(1, 'text', subBuilder: Text.create)
    ..aOM<Document>(2, 'document', subBuilder: Document.create)
    ..hasRequiredFields = false
  ;

  ParseResult._() : super();
  factory ParseResult() => create();
  factory ParseResult.fromBuffer($core.List<$core.int> i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromBuffer(i, r);
  factory ParseResult.fromJson($core.String i, [$pb.ExtensionRegistry r = $pb.ExtensionRegistry.EMPTY]) => create()..mergeFromJson(i, r);
  ParseResult clone() => ParseResult()..mergeFromMessage(this);
  ParseResult copyWith(void Function(ParseResult) updates) => super.copyWith((message) => updates(message as ParseResult));
  $pb.BuilderInfo get info_ => _i;
  @$core.pragma('dart2js:noInline')
  static ParseResult create() => ParseResult._();
  ParseResult createEmptyInstance() => create();
  static $pb.PbList<ParseResult> createRepeated() => $pb.PbList<ParseResult>();
  @$core.pragma('dart2js:noInline')
  static ParseResult getDefault() => _defaultInstance ??= $pb.GeneratedMessage.$_defaultFor<ParseResult>(create);
  static ParseResult _defaultInstance;

  @$pb.TagNumber(1)
  Text get text => $_getN(0);
  @$pb.TagNumber(1)
  set text(Text v) { setField(1, v); }
  @$pb.TagNumber(1)
  $core.bool hasText() => $_has(0);
  @$pb.TagNumber(1)
  void clearText() => clearField(1);
  @$pb.TagNumber(1)
  Text ensureText() => $_ensure(0);

  @$pb.TagNumber(2)
  Document get document => $_getN(1);
  @$pb.TagNumber(2)
  set document(Document v) { setField(2, v); }
  @$pb.TagNumber(2)
  $core.bool hasDocument() => $_has(1);
  @$pb.TagNumber(2)
  void clearDocument() => clearField(2);
  @$pb.TagNumber(2)
  Document ensureDocument() => $_ensure(1);
}


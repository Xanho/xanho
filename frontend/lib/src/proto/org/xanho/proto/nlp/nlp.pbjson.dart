///
//  Generated code. Do not modify.
//  source: org/xanho/proto/nlp/nlp.proto
//
// @dart = 2.3
// ignore_for_file: camel_case_types,non_constant_identifier_names,library_prefixes,unused_import,unused_shown_name,return_of_invalid_type

const Text$json = const {
  '1': 'Text',
  '2': const [
    const {'1': 'text', '3': 1, '4': 1, '5': 9, '10': 'text'},
  ],
};

const Token$json = const {
  '1': 'Token',
  '2': const [
    const {'1': 'space', '3': 1, '4': 1, '5': 11, '6': '.org.xanho.proto.nlp.Token.Space', '9': 0, '10': 'space'},
    const {'1': 'lineBreak', '3': 2, '4': 1, '5': 11, '6': '.org.xanho.proto.nlp.Token.LineBreak', '9': 0, '10': 'lineBreak'},
    const {'1': 'punctuation', '3': 3, '4': 1, '5': 11, '6': '.org.xanho.proto.nlp.Token.Punctuation', '9': 0, '10': 'punctuation'},
    const {'1': 'word', '3': 4, '4': 1, '5': 11, '6': '.org.xanho.proto.nlp.Token.Word', '9': 0, '10': 'word'},
  ],
  '3': const [Token_Space$json, Token_LineBreak$json, Token_Punctuation$json, Token_Word$json],
  '8': const [
    const {'1': 'value'},
  ],
};

const Token_Space$json = const {
  '1': 'Space',
};

const Token_LineBreak$json = const {
  '1': 'LineBreak',
};

const Token_Punctuation$json = const {
  '1': 'Punctuation',
  '2': const [
    const {'1': 'value', '3': 1, '4': 1, '5': 9, '10': 'value'},
  ],
};

const Token_Word$json = const {
  '1': 'Word',
  '2': const [
    const {'1': 'value', '3': 1, '4': 1, '5': 9, '10': 'value'},
  ],
};

const Phrase$json = const {
  '1': 'Phrase',
  '2': const [
    const {'1': 'words', '3': 1, '4': 3, '5': 11, '6': '.org.xanho.proto.nlp.Token.Word', '10': 'words'},
  ],
};

const Sentence$json = const {
  '1': 'Sentence',
  '2': const [
    const {'1': 'phrase', '3': 1, '4': 1, '5': 11, '6': '.org.xanho.proto.nlp.Phrase', '10': 'phrase'},
    const {'1': 'punctuation', '3': 2, '4': 1, '5': 11, '6': '.org.xanho.proto.nlp.Token.Punctuation', '10': 'punctuation'},
    const {'1': 'sentenceType', '3': 3, '4': 1, '5': 14, '6': '.org.xanho.proto.nlp.Sentence.SentenceType', '10': 'sentenceType'},
  ],
  '4': const [Sentence_SentenceType$json],
};

const Sentence_SentenceType$json = const {
  '1': 'SentenceType',
  '2': const [
    const {'1': 'STATEMENT', '2': 0},
    const {'1': 'QUESTION', '2': 1},
  ],
};

const Paragraph$json = const {
  '1': 'Paragraph',
  '2': const [
    const {'1': 'sentences', '3': 1, '4': 3, '5': 11, '6': '.org.xanho.proto.nlp.Sentence', '10': 'sentences'},
  ],
};

const Document$json = const {
  '1': 'Document',
  '2': const [
    const {'1': 'paragraphs', '3': 1, '4': 3, '5': 11, '6': '.org.xanho.proto.nlp.Paragraph', '10': 'paragraphs'},
  ],
};

const ParseResult$json = const {
  '1': 'ParseResult',
  '2': const [
    const {'1': 'text', '3': 1, '4': 1, '5': 11, '6': '.org.xanho.proto.nlp.Text', '10': 'text'},
    const {'1': 'document', '3': 2, '4': 1, '5': 11, '6': '.org.xanho.proto.nlp.Document', '10': 'document'},
  ],
};


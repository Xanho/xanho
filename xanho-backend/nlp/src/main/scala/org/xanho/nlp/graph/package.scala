package org.xanho.nlp

package object graph {
  object NodeTypes {
    val Word = "word"
    val Punctuation = "punctuation"
    val Phrase = "phrase"
    val Sentence = "sentence"
    val Paragraph = "paragraph"
    val Document = "document"
  }

  object EdgeTypes {
    val PhraseWord = "phraseWord"
    val SentencePunctuation = "sentencePunctuation"
    val SentencePhrase = "sentencePhrase"
    val ParagraphSentence = "paragraphSentence"
    val DocumentParagraph = "documentParagraph"
  }

}

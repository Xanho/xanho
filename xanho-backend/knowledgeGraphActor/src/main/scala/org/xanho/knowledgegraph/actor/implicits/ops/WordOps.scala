package org.xanho.knowledgegraph.actor.implicits.ops

import org.xanho.proto.nlp.Token

class WordOps(word: Token.Word) {

  def toLowerCase: Token.Word =
    word.copy(value = word.value.toLowerCase())

}

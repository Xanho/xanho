package org.xanho.nlp.graph

import org.xanho.proto.graph.Node

package object ops {

  object implicits
    extends NlpGraphQueryOps
      with NlpGraphBuilderOps
      with NlpNodeOps

  type WordNode = Node
  type PunctuationNode = Node
  type PhraseNode = Node
  type SentenceNode = Node
  type ParagraphNode = Node
  type DocumentNode = Node

}

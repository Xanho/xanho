package org.xanho.nlp

package object ops {

  object implicits
    extends IterableOps
      with NlpCharOps
      with NlpStringOps
      with TokenizerOps
      with TokenSeqOps

}

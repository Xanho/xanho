package org.xanho.graph

package object ops {

  object implicits
    extends GraphOps
      with NodeOps
      with EdgeOps

}

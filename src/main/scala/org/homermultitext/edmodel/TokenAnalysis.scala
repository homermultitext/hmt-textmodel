package org.homermultitext.edmodel

import edu.holycross.shot.cite._

case class  TokenAnalysis(textNode: CtsUrn, analysis: HmtToken) {

  def longString = {
    "Context = "+ textNode.toString + "\n" + analysis.orcaColumn + "\n\n\n"
  }
}

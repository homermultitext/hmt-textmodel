package org.homermultitext.edmodel

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

case class  TokenAnalysis(textNode: CtsUrn, analysis: HmtToken) {

  def longString = {
    "Context = "+ textNode.toString + "\n" + analysis.orcaColumn + "\n\n\n"
  }
  def hasAlternate: Boolean = {
    analysis.hasAlternate
  }

  def lexMatch(urn: Cite2Urn): Boolean = {
    analysis lexMatch urn
  }

  def simpleText: CitableNode = {
    CitableNode(analysis.editionUrn, analysis.singleReading)
  }
}

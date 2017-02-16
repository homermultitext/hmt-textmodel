package org.homermultitext.edmodel

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

case class  TokenAnalysis(textNode: CtsUrn, analysis: HmtToken) {

  def longString = {
    "Context = "+ textNode.toString + "\n" + analysis.orcaColumn + "\n\n\n"
  }
  def readWithAlternate: CitableNode = {
    CitableNode(analysis.editionUrn, analysis.readWithAlternate)
  }
  def readWithDiplomatic: CitableNode = {
    CitableNode(analysis.editionUrn, analysis.readWithDiplomatic)
  }

  def hasAlternate: Boolean = {
    analysis.hasAlternate
  }
  def isAbbreviation: Boolean = {
    analysis.isAbbreviation
  }
  def hasScribalMultiform: Boolean = {
    analysis.hasScribalMultiform
  }
  def hasScribalCorrection: Boolean = {
    analysis.hasScribalCorrection
  }

  def lexMatch(urn: Cite2Urn): Boolean = {
    analysis lexMatch urn
  }
}

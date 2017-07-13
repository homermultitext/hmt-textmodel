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
  def readWithScribal: CitableNode = {
    CitableNode(analysis.editionUrn, analysis.readWithScribal)
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



  def isCitation: Boolean = {
    (analysis.discourse == Citation)
  }
  def isQuotedText: Boolean = {
    (analysis.discourse == QuotedText)
  }
  def isQuotedLiteral: Boolean = {
    (analysis.discourse == QuotedLiteral)
  }
  def isQuotedLanguage: Boolean = {
    (analysis.discourse == QuotedLanguage)
  }
  def isDirectVoice: Boolean = {
    (analysis.discourse == DirectVoice)
  }
  def notDirectVoice:  Boolean = {
    (analysis.discourse != DirectVoice)
  }

  def lexMatch(urn: Cite2Urn): Boolean = {
    analysis lexMatch urn
  }

  def columnString: String = {
    analysis.columnString
  }
}

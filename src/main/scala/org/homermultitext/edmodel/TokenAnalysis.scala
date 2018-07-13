package org.homermultitext.edmodel

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._


/** An analysis of a single token.
*
* @param textNode CtsUrn of the citable node where this token occurs.
* Note that this will always be equivalent to the version-level URN for containing node for the "edition URN" of the [[HmtToken]], since the
* edition URN extends the passage hierarchy with a "tokens" exemplar,
* and extends the passage hierarchy with a further level.
*
* Expressed in code, we can say that for any [[TokenAnalysis]] ta,
* the following relation is true:
*  {{{
*   ta.analysis.editionUrn.collapsePassageBy(1) == ta.textNode.addExemplar("tokens")
*
*  }}}
* @param analysis The analysis of this token as a full [[HmtToken]]
* object.
*/
case class  TokenAnalysis(textNode: CtsUrn, analysis: HmtToken) {



  /** Create CitableNode for this token
  * based on reading with alternate reading,
  * if one exists.
  */
  def readWithAlternate: CitableNode = {
    CitableNode(analysis.editionUrn, analysis.readWithAlternate)
  }


  // These need help
  def readWithDiplomatic: Option[CitableNode] = {
    val txt = analysis.readWithDiplomatic
    if (txt.nonEmpty){
      Some(CitableNode(analysis.editionUrn, txt))
    } else {
      None
    }
  }
  def readWithScribal: Option[CitableNode] = {
    val txt = analysis.readWithScribal
    if (txt.nonEmpty){
      Some(CitableNode(analysis.editionUrn, analysis.readWithScribal))
    } else {
      None
    }
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

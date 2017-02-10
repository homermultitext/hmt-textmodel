package org.homermultitext.edmodel

import edu.holycross.shot.cite._

/** token in an ORCA analytical exemplar
*
* @param urn exemplar-level URN identifying this token in a specific reading of a
* HMT edition
* @param src URN of passage read or analyzed
* @param textDeformation string view of this token
* @param hmtToken full analysis of this token
*/
case class HmtOrcaToken (
  urn: CtsUrn,
  src: CtsUrn,
  textDeformation: String,
  hmtToken: HmtToken) {

    def orcaColumn: String = {
      val analysisCols =  hmtToken.columnString.split("\n").slice(3,10).toVector.mkString("\n")
      "Passage: " + urn + "\nAnalyzes: " + src + "\nReading: " + textDeformation + "\nAnalyses as: \n" + analysisCols
    }

    def rowString(separator: String = "\t"): String = {
      urn.toString +separator + src.toString + separator + textDeformation
    }
  }


object HmtOrcaToken {

  def sourceFromToken(tkn: HmtToken, edKey: String ) = {
    if (edKey == "token") {
      tkn.sourceUrn
    } else {
      tkn.editionUrn
    }
  }

  def urnFromToken(tkn: HmtToken, edKey: String) = {
    if (edKey == "token") {
      tkn.editionUrn
    } else {
      CtsUrn("urn:cts:greekLit:" + tkn.editionUrn.workComponent + "." + edKey + ":" + tkn.editionUrn.passageComponent)
    }
  }

  def apply(hmtToken : HmtToken, readingKey: String): HmtOrcaToken = {
    val u: CtsUrn = urnFromToken(hmtToken, readingKey)
    val src: CtsUrn = sourceFromToken(hmtToken, readingKey)
    val deform = TextDeformation(hmtToken,readingKey)
    HmtOrcaToken(u,src,deform.text,hmtToken)
  }
}

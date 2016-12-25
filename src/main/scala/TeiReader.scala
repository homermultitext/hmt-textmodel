package org.homermultitext.edmodel


import scala.collection.mutable.ArrayBuffer
import scala.xml._

object TeiReader {

  var tokenBuffer = scala.collection.mutable.ArrayBuffer.empty[HmtToken]

  def collectTokens(currToken: HmtToken, n: xml.Node): Unit = {
    n match {
      case t: xml.Text => {
        val tokenList = t.text.split("[ ]+").filterNot(_.isEmpty)
        for (tk <- tokenList) {
          val rdg = Reading(tk, Clear)
          var newToken = currToken.copy(readings = Vector(rdg))
          tokenBuffer += newToken
        }
      }
      case e: xml.Elem => {
        for (ch <- e.child) {
          collectTokens(currToken, ch)
        }
      }
    }
  }


  def teiToTokens(urnStr: String, xmlStr: String) : Vector[ (String, HmtToken)]  = {
    val root  = XML.loadString(xmlStr)
    val currToken = HmtToken(
      urn = urnStr,
      lexicalCategory = LexicalToken,
      readings = Vector.empty
    )
    tokenBuffer.clear
    collectTokens(currToken, root)

    // in the final result, add exemplar-level
    // citation element
    val zippedVal = tokenBuffer.zipWithIndex.map{ case (t,i) => {
      val baseUrn = t.urn
      t.urn = baseUrn + "." + (i +1)
      (baseUrn, t) }
    }.toVector


    zippedVal

  }
}

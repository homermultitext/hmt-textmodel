package org.homermultitext.edmodel


import scala.collection.mutable.ArrayBuffer
import scala.xml._

object TeiReader {

  var tokenBuffer = scala.collection.mutable.ArrayBuffer.empty[HmtToken]

  var wrappedWordBuffer = scala.collection.mutable.ArrayBuffer.empty[Reading]

  def collectWrappedWordStrings(editorialStatus: EditorialStatus, n: xml.Node): Unit = {
    n match {
      case t: xml.Text => {
        val readingString = t.text.replaceAll(" ", "")
        if (! readingString.isEmpty) {
          wrappedWordBuffer += Reading(readingString  , editorialStatus)
        }
      }

      case e: xml.Elem => {
        e.label match {
          case "unclear" => {
            for (ch <- e.child) {
              collectWrappedWordStrings(Unclear,ch)
            }
          }
          case _ => {
            for (ch <- e.child) {
              collectWrappedWordStrings(editorialStatus,ch)
            }
          }
        }
      }
    }
  }



  def collectTokens(currToken: HmtToken, n: xml.Node): Unit = {
    n match {
      case t: xml.Text => {
        // the awesomeness of regex: split on set of
        // characters without losing them:
        val depunctuate =   t.text.split("((?<=[,;⁑\\.])|(?=[,;⁑\\.]))")
        val tokenList = depunctuate.flatMap(_.split("[ ]+")).filterNot(_.isEmpty)
        for (tk <- tokenList) {
          val rdg = Reading(tk, Clear)
          var newToken = currToken.copy(readings = Vector(rdg))
          tokenBuffer += newToken
        }
      }
      case e: xml.Elem => {
        e.label match {
          case "num" => {
            val newToken = currToken.copy(lexicalCategory = NumericToken)
            for (ch <- e.child) {
              collectTokens(newToken, ch)
            }
          }
          case "w" => {
            wrappedWordBuffer.clear
            collectWrappedWordStrings(Clear,e)
            val newToken = currToken.copy(readings = wrappedWordBuffer.toVector)
            tokenBuffer += newToken
          }
          case l: String =>  {
            for (ch <- e.child) {
              collectTokens(currToken, ch)
            }
          }
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

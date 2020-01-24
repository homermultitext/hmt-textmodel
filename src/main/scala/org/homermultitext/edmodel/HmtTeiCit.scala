package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import scala.xml._


import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


object HmtTeiCit extends LogSupport{
Logger.setDefaultLogLevel(LogLevel.INFO)
  def ctsUrnOk(urnStr: String) : Boolean = {
    try {
      val u = CtsUrn(urnStr)
      true
    } catch {
      case t : Throwable => {
        warn("Something wrong with CtsUrn value " + urnStr + ".  " + t)
        false
      }
    }
  }


  def citChildren(citElem: scala.xml.Elem): Vector[String] = {
    citElem.child.map(_.label).distinct.filterNot(_ == "#PCDATA").toVector
  }

  def citeChildren = Set("q", "ref")
  // check structure of TEI cit
  def validCit(citElem: scala.xml.Elem): Boolean = {
    val chNames = citChildren(citElem).toSet
    debug("Valid cit: child names " + chNames)
    if (citeChildren == chNames ) {
      true
    } else {
      false
    }
  }


  // use this only after you've verified that the element has two appropriately
  // matched child elements
  def pairedToken(el: scala.xml.Elem,  settings: TokenSettings): Vector[HmtToken] = {
    val ch1 = el.child(0)
    val ch2 = el.child(1)

    val type1Val = (ch1 \ "@type").text
    if ( type1Val == "urn") {
      if (ctsUrnOk(ch1.text)) {
        val urn = CtsUrn(ch1.text)
        val newSettings = settings.addExternalSource(Some(urn)).setDiscourse(QuotedLanguage)
        val tkns = for (ch <- ch2.child) yield {
          TeiReader.collectTokens(ch, newSettings)
        }
        tkns.toVector.flatten

      } else {
        val errMsg = "Valid URN value required for content of 'ref' element with 'cit'."
        val newSettings = settings.addError(errMsg)
        val tkns = for (ch <- el.child) yield {
          TeiReader.collectTokens(ch, newSettings)
        }
        tkns.toVector.flatten
      }

    } else{
      if (ctsUrnOk(ch2.text)) {
        val urn = CtsUrn(ch2.text)
        val newSettings = settings.addExternalSource(Some(urn)).setDiscourse(QuotedLanguage)
        val tkns = for (ch <- ch1.child) yield {
          TeiReader.collectTokens(ch, newSettings)
        }
        tkns.toVector.flatten

      } else {
        val errMsg = "Valid URN value required for content of 'ref' element with 'cit'."
        val newSettings = settings.addError(errMsg)
        val tkns = for (ch <- el.child) yield {
          TeiReader.collectTokens(ch, newSettings)
        }
        tkns.toVector.flatten
      }
    }
  }

}

package org.homermultitext.edmodel


import scala.collection.mutable.ArrayBuffer
import scala.xml._

object TeiReader {
  // perhaps should be a function retrieving
  // list by text group and lexical category?
  val punctuation = Vector(",",".",";","⁑")

  // only need list of elements *not* explicitly
  // caught in big case match below
  val validElements = Vector(
    "div", "l","p", "choice",
    "foreign",
    "num",
    "unclear","add","orig","reg","sic","corr",
    "abbr","expan",
    "cite","q","ref",
    "persName","placeName",
    "rs"
  )

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
  def abbrExpanChoice(hmtToken: HmtToken, el: xml.Elem) = {
    val abbrSeq = el \ "abbr"
    val abbr = abbrSeq(0)
    val expanSeq = el \ "expan"
    val expan  = expanSeq(0)
    val expandedReading = Reading(expan.text,Restored)
    val alt = AlternateReading(Restoration,Vector(expandedReading))
    val newToken = hmtToken.copy(alternateReading = alt)
    collectTokens(newToken,abbr)
  }

  def sicCorrChoice(hmtToken: HmtToken, el: xml.Elem) = {
    val sicSeq = el \ "sic"
    val sic = sicSeq(0)
    val corrSeq = el \ "corr"
    val corr  = corrSeq(0)

    wrappedWordBuffer.clear
    collectWrappedWordStrings(Clear,corr)
    val alt = AlternateReading(Correction,wrappedWordBuffer.toVector)
    wrappedWordBuffer.clear

    val newToken = hmtToken.copy(alternateReading = alt)
    collectTokens(newToken,sic)
  }

  def origRegChoice(hmtToken: HmtToken, el: xml.Elem) = {
    val origSeq = el \ "orig"
    val orig = origSeq(0)
    val regSeq = el \ "reg"
    val reg  = regSeq(0)


    wrappedWordBuffer.clear
    collectWrappedWordStrings(Clear,reg)
    val alt = AlternateReading(Multiform,wrappedWordBuffer.toVector)
    wrappedWordBuffer.clear

    val newToken = hmtToken.copy(alternateReading = alt)
    collectTokens(newToken,orig)


  }

  def getAlternate (hmtToken: HmtToken, n: xml.Elem) = {
    val cNames = n.child.map(_.label).distinct.filterNot(_ == "#PCDATA")

    val abbrExpan = Array("abbr","expan")
    val sicCorr = Array("sic", "corr")
    val origReg = Array("orig", "reg")

    if (cNames.sameElements(abbrExpan) ) {
      abbrExpanChoice(hmtToken, n)
    } else if (cNames.sameElements(sicCorr) ) {
      sicCorrChoice(hmtToken, n)
    } else if (cNames.sameElements(origReg) ) {
      origRegChoice(hmtToken,n)

    } else {
      println("BAD choice : " + cNames)
    }
  }

  def getCited(currToken: HmtToken, el: xml.Elem) {
    val citeStruct = Array("ref","q")
    val cNames = el.child.map(_.label).distinct.filterNot(_ == "#PCDATA")


    if (cNames.sameElements(citeStruct) ) {
      val refs = el \ "ref"
      val srcRef = refs(0).text.trim
      val newToken = currToken.copy(discourse = CitedText,
      externalSource = srcRef)
      for (ch <- el.child) {
        collectTokens(newToken, ch)
      }

    } else {
      var errorList = currToken.errors :+  "Invalid structure: cit should have both q and ref children"
      val newToken = currToken.copy(discourse = CitedText,
      errors = errorList)
      for (ch <- el.child) {
        collectTokens(newToken, ch)
      }
    }
  }

  def collectTokens(currToken: HmtToken, n: xml.Node): Unit = {
    n match {
      case t: xml.Text => {
        // the awesomeness of regex: split on set of
        // punctuation characters without losing them:
        val depunctuate =   t.text.split("((?<=[,;⁑\\.])|(?=[,;⁑\\.]))")
        val tokenList = depunctuate.flatMap(_.split("[ ]+")).filterNot(_.isEmpty)
        for (tk <- tokenList) {
          val rdg = Reading(tk, Clear)
          var newToken = currToken.copy(readings = Vector(rdg))
          if (punctuation.contains(tk)) {
            newToken.lexicalCategory = Punctuation
          }
          tokenBuffer += newToken
        }
      }
      case e: xml.Elem => {
        e.label match {
          case "q" => {


            currToken.discourse match {
                case CitedText => { // don't change!
                  for (ch <- e.child) {
                    collectTokens(currToken, ch)
                  }
                }
                case _ => {
                  val newToken = currToken.copy(discourse = QuotedLanguage)
                  for (ch <- e.child) {
                    collectTokens(newToken, ch)
                  }
                }
            }
          }
          case "cit" => {
            getCited(currToken,e)
          }
          case "ref" => {}
          case "num" => {
            val newToken = currToken.copy(lexicalCategory = NumericToken, lexicalDisambiguation = "Automated numeric parsing")
            for (ch <- e.child) {
              collectTokens(newToken, ch)
            }
          }
          case "sic" => {
            val newToken = currToken.copy(lexicalCategory = Unintelligible)
            for (ch <- e.child) {
              collectTokens(newToken, ch)
            }
          }
          case "rs" => {
            val typeAttrs = e \ "@type"
            typeAttrs(0).text match {
              case "waw" => {
                val newToken = currToken.copy(lexicalCategory = LiteralToken)
                for (ch <- e.child) {
                  collectTokens(newToken, ch)
                }
              }
              case "ethnic" => {
                val nAttrs = e \ "@n"
                val newToken = currToken.copy(lexicalDisambiguation = nAttrs(0).text)
                for (ch <- e.child) {
                  collectTokens(newToken, ch)
                }
              }
              case _ => {
                for (ch <- e.child) {
                  collectTokens(currToken, ch)
                }
              }
            }

          }
          case "w" => {
            wrappedWordBuffer.clear
            collectWrappedWordStrings(Clear,e)
            val newToken = currToken.copy(readings = wrappedWordBuffer.toVector)
            tokenBuffer += newToken
          }
          case "foreign" => {
            val langAttributes = e.attributes.toVector.filter(_.key == "lang").map(_.value)
            require (langAttributes.size == 1)
            val langVal = langAttributes(0).text
            val newToken = currToken.copy(lang = langVal)
            for (ch <- e.child) {
              collectTokens(newToken, ch)
            }
          }

          case "choice" => {
            getAlternate(currToken,e)
          }


          case "persName" => {
            val nAttrs = e \ "@n"
            val newToken = currToken.copy(lexicalDisambiguation = nAttrs(0).text)
            for (ch <- e.child) {
              collectTokens(newToken, ch)
            }
          }

          case "placeName" => {
            val nAttrs = e \ "@n"
            val newToken = currToken.copy(lexicalDisambiguation = nAttrs(0).text)
            for (ch <- e.child) {
              collectTokens(newToken, ch)
            }
          }
          case l: String =>  {
            if (validElements.contains(l)) {
              for (ch <- e.child) {
                collectTokens(currToken, ch)
              }
            } else {
              var errorList = currToken.errors :+  "Invalid element name: " + l
              val newToken = currToken.copy(errors = errorList)
              for (ch <- e.child) {
                collectTokens(newToken, ch)
              }
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

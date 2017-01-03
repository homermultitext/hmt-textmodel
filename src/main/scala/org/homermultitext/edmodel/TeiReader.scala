package org.homermultitext.edmodel


import scala.collection.mutable.ArrayBuffer
import scala.xml._
import scala.io.Source

import edu.holycross.shot.cite._


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

  /** Recursively collects contents of all text-node
  * descendants of a given node.
  * @param n Node to collect from.
  * @param buff Buffer for collecting text contents.
  * @returns A single String with all text from n.
  */
  def collectText(n: xml.Node, s: String): String = {
    println("cOLLEcT: " + n.label + " with " + s)
    var buff = StringBuilder.newBuilder
    buff.append(s)
    n match {
      case t: xml.Text => {
        buff.append(t.text)
        println("After append, buf is " + buff.toString)
      }

      case e: xml.Elem => {
        for (ch <- e.child) {
          buff = new StringBuilder(collectText(ch, buff.toString))
        }
      }
    }
    println("Return " + buff.toString)
    buff.toString
  }

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
      val newToken = currToken.copy(discourse = QuotedText,
      externalSource = srcRef)
      for (ch <- el.child) {
        collectTokens(newToken, ch)
      }

    } else {
      var errorList = currToken.errors :+  "Invalid structure: cit should have both q and ref children"
      val newToken = currToken.copy(discourse = QuotedText,
      errors = errorList)
      for (ch <- el.child) {
        collectTokens(newToken, ch)
      }
    }
  }


  def collectRefString(currToken: HmtToken, el: xml.Elem) = {
    val typeAttrs = el \ "@type"
    if (typeAttrs.size == 0) {
      var errorList = currToken.errors :+ "rs element missing required @type attribute"
      val newToken = currToken.copy(errors = errorList)
      for (ch <- el.child) {
        collectTokens(newToken, ch)
      }

    } else {
     typeAttrs(0).text match {
       case "waw" => {
         val newToken = currToken.copy(lexicalCategory = LiteralToken)
         for (ch <- el.child) {
           collectTokens(newToken, ch)
         }
       }

       case "ethnic" => {
         disambiguateNamedEntity(currToken,el)
       }

       case _ => {
         for (ch <- el.child) {
           collectTokens(currToken, ch)
         }
       }
     }
   }
  }

  def disambiguateNamedEntity(currToken: HmtToken, el: xml.Elem) {
    val nAttrs = el \ "@n"
    if (nAttrs.size < 1) {
      var errorList = currToken.errors :+ "element " + el.label + " missing required @n attribute"
      val newToken = currToken.copy(errors = errorList)
      for (ch <- el.child) {
        collectTokens(newToken, ch)
      }

    } else {
      val newToken = currToken.copy(lexicalDisambiguation = nAttrs(0).text)
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
          case "add" => {
            //  multiform
            //
            wrappedWordBuffer.clear
            collectWrappedWordStrings(Clear,e)
            val alt = AlternateReading(Multiform,wrappedWordBuffer.toVector)
            wrappedWordBuffer.clear
          }

          case "q" => {
            currToken.discourse match {
                case QuotedText => { // don't change!
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
            collectRefString(currToken,e )
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
            disambiguateNamedEntity(currToken,e)
          }
          case "placeName" => {
            disambiguateNamedEntity(currToken,e)
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



  /** Converts one well-formed
  * fragment of TEI XML following HMT conventions
  * to an ordered sequence of (URN,HmtToken) tuples.
  */
  def teiToTokens(urnStr: String, xmlStr: String) : Vector[ (CtsUrn, HmtToken)]  = {
    val root  = XML.loadString(xmlStr)
    val currToken = HmtToken(
      urn = CtsUrn(urnStr),
      sourceSubref = CtsUrn(urnStr + "@" + "UNSPECIFIED"),
      analysis = "CITE URN GOES HERE",
      lexicalCategory = LexicalToken,
      readings = Vector.empty
    )
    tokenBuffer.clear
    collectTokens(currToken, root)

    // in the final result, add exemplar-level
    // citation element
    val zippedVal = tokenBuffer.zipWithIndex.map{ case (t,i) => {
      val baseUrn = t.urn
      t.urn = CtsUrn(baseUrn.toString + "." + (i +1))
      (baseUrn, t) }
    }.toVector

    zippedVal
  }


  def fromTwoColumns(fileName: String): Vector[(CtsUrn, HmtToken)] = {
    fromTwoColumns(fileName,"\t")
  }

  def fromTwoColumns(fileName: String, separator: String): Vector[(CtsUrn, HmtToken)] = {
    val pairArray = scala.io.Source.fromFile(fileName).getLines.toVector.map(_.split(separator))
    pairArray.flatMap( arr => TeiReader.teiToTokens(arr(0), arr(1)))

  }
}

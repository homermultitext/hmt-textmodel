package org.homermultitext.edmodel


import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map
import scala.xml._
import scala.io.Source

import edu.holycross.shot.cite._

/**  Factory for Vectors of  [[org.homermultitext.edmodel.HmtToken]] instances.
*/
object TeiReader {

  var nodeText: String = ""
  var tokenIndexCount = scala.collection.mutable.Map[String, Int]()



  /** buffer for recursively accumulated [[org.homermultitext.edmodel.HmtToken]]s
  */
  var tokenBuffer = scala.collection.mutable.ArrayBuffer.empty[HmtToken]

  /** buffer for recursively accumulated [[org.homermultitext.edmodel.Reading]]s
  * for a single token */
  var wrappedWordBuffer = scala.collection.mutable.ArrayBuffer.empty[Reading]

  /** awesome regular expression to split a string on
  * HMT Greek punctuation characters while keeping the
  * punctuation characters as individual tokens.
  */
  val punctuationSplitter = "((?<=[,;⁑\\.])|(?=[,;⁑\\.]))"

  /** recursively collect all [[org.homermultitext.edmodel.Reading]] objects descended
  * from a given node, and add a Vector of [[org.homermultitext.edmodel.Reading]]s
  * to the TeiReader's `wrappedWordBuffer`
  *
  * @param editorialStatus editorial status of surrounding context
  * @param n node to descend from
  */
  def collectWrappedWordReadings(editorialStatus: EditorialStatus, n: xml.Node): Unit = {
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
              collectWrappedWordReadings(Unclear,ch)
            }
          }
          case _ => {
            for (ch <- e.child) {
              collectWrappedWordReadings(editorialStatus,ch)
            }
          }
        }
      }
    }
  }


  /** collect tokens from a TEI `abbr-expan` pair
  *
  * Results are added to the TeiReader's `tokenBuffer`.
  *
  * @param hmtToken token reflecting reading values for parent context
  * @param el TEI `choice` element with `abbr-expan` children
  */
  def abbrExpanChoice(hmtToken: HmtToken, el: xml.Elem) = {
    val abbrSeq = el \ "abbr"
    val abbr = abbrSeq(0)
    val expanSeq = el \ "expan"
    val expan  = expanSeq(0)
    val expandedReading = Reading(expan.text,Restored)
    val alt = AlternateReading(Restoration,Vector(expandedReading))
    val newToken = hmtToken.copy(alternateReading = Some(alt))
    collectTokens(newToken,abbr)
  }

  /** collect tokens from a TEI `sic-corr` pair
  *
  * Results are added to the TeiReader's `tokenBuffer`.
  *
  * @param hmtToken token reflecting reading values for parent context
  * @param el TEI `choice` element with `sic-corr` children
  */
  def sicCorrChoice(hmtToken: HmtToken, el: xml.Elem) = {
    val sicSeq = el \ "sic"
    val sic = sicSeq(0)
    val corrSeq = el \ "corr"
    val corr  = corrSeq(0)

    wrappedWordBuffer.clear
    collectWrappedWordReadings(Clear,corr)
    val alt = AlternateReading(Correction,wrappedWordBuffer.toVector)
    wrappedWordBuffer.clear

    val newToken = hmtToken.copy(alternateReading = Some(alt))
    collectTokens(newToken,sic)
  }

  /** collect tokens from a TEI `orig-reg` pair
  *
  * Results are added to the TeiReader's `tokenBuffer`.
  *
  * @param hmtToken token reflecting reading values for parent context
  * @param el TEI `choice` element with `orig-reg` children
  */
  def origRegChoice(hmtToken: HmtToken, el: xml.Elem) = {
    val origSeq = el \ "orig"
    val orig = origSeq(0)
    val regSeq = el \ "reg"
    val reg  = regSeq(0)

    wrappedWordBuffer.clear
    collectWrappedWordReadings(Clear,reg)
    val alt = AlternateReading(Multiform,wrappedWordBuffer.toVector)
    wrappedWordBuffer.clear

    val newToken = hmtToken.copy(alternateReading = Some(alt))
    collectTokens(newToken,orig)
  }


  /** get alternates as well as tokens from a TEI `choice` element
  *
  * @param hmtToken token reflecting reading values for parent context
  * @param choiceElem TEI `choice` element
  */
  def getAlternate (hmtToken: HmtToken, choiceElem: xml.Elem) = {
    val cNames = choiceElem.child.map(_.label).distinct.filterNot(_ == "#PCDATA")

    val abbrExpan = Array("abbr","expan")
    val sicCorr = Array("sic", "corr")
    val origReg = Array("orig", "reg")

    if (cNames.sameElements(abbrExpan) ) {
      abbrExpanChoice(hmtToken, choiceElem)
    } else if (cNames.sameElements(sicCorr) ) {
      sicCorrChoice(hmtToken, choiceElem)
    } else if (cNames.sameElements(origReg) ) {
      origRegChoice(hmtToken,choiceElem)

    } else {
      println("BAD choice : " + cNames)
    }
  }

  /** collect tokens from cited context
  *
  * @param currToken token reflecting reading values for parent context
  * @param citElem TEI `cit` element
  */
  def collectCited(currToken: HmtToken, citElem: xml.Elem) {
    val citeStruct = Array("ref","q")
    val cNames = citElem.child.map(_.label).distinct.filterNot(_ == "#PCDATA")

    if (cNames.sameElements(citeStruct) ) {
      val refs = citElem \ "ref"
      val srcRef = refs(0).text.trim
      try {
        val u = CtsUrn(srcRef)
        val newToken = currToken.copy(discourse = QuotedText,
        externalSource = Some(u))
        for (ch <- citElem.child) {
          collectTokens(newToken, ch)
        }
      } catch {
        case badarg: java.lang.IllegalArgumentException => {
          println(badarg)
          var errorList = currToken.errors :+  "Exception: " + badarg
          val newToken = currToken.copy(discourse = QuotedText,
          errors = errorList)
          for (ch <- citElem.child) {
            collectTokens(newToken, ch)
          }
        }
        case ex: Exception => {
          println("Unrecognized exception " + ex)
          var errorList = currToken.errors :+  "Exception: " + ex
          val newToken = currToken.copy(discourse = QuotedText,
          errors = errorList)
          for (ch <- citElem.child) {
            collectTokens(newToken, ch)
          }
        }
      }




    } else {
      var errorList = currToken.errors :+  "Invalid structure: cit should have both q and ref children"
      val newToken = currToken.copy(discourse = QuotedText,
      errors = errorList)
      for (ch <- citElem.child) {
        collectTokens(newToken, ch)
      }
    }
  }


  /** collect appropriate type of token for varieties of TEI `rs` usage
  *
  * @param currToken token reflecting reading values for parent context
  * @param rsElem TEI `rs` element
  */
  def collectRefString(currToken: HmtToken, rsElem: xml.Elem) = {
    val typeAttrs = rsElem \ "@type"
    if (typeAttrs.size == 0) {
      var errorList = currToken.errors :+ "rs element missing required @type attribute"
      val newToken = currToken.copy(errors = errorList)
      for (ch <- rsElem.child) {
        collectTokens(newToken, ch)
      }

    } else {
     typeAttrs(0).text match {
       case "waw" => {
         val newToken = currToken.copy(lexicalCategory = LiteralToken)
         for (ch <- rsElem.child) {
           collectTokens(newToken, ch)
         }
       }

       case "ethnic" => {
         disambiguateNamedEntity(currToken,rsElem)
       }

       case s: String => {
         var errorList = currToken.errors :+ "unrecognized value for @type attribute on rs element " + s
         val newToken = currToken.copy(errors = errorList)
         for (ch <- rsElem.child) {
           collectTokens(newToken, ch)
         }
       }
     }
   }
  }


  /** collect tokens with appropriate disambiguation for varieties of named entities
  *
  * @param currToken token reflecting reading values for parent context
  * @param el a TEI element disambiguating a named entity.
  * Should be one of `persName`, `placeName` or `rs` with `type` = `ethnic`
  */
  def disambiguateNamedEntity(currToken: HmtToken, el: xml.Elem) {
    val nAttrs = el \ "@n"
    if (nAttrs.size < 1) {
      var errorList = currToken.errors :+ "element " + el.label + " missing required @n attribute"
      val newToken = currToken.copy(errors = errorList)
      for (ch <- el.child) {
        collectTokens(newToken, ch)
      }

    } else {
      try {
        val newToken = currToken.copy(lexicalDisambiguation = CiteUrn(nAttrs(0).text))
        for (ch <- el.child) {
          collectTokens(newToken, ch)
        }
      } catch {
        case badarg: java.lang.IllegalArgumentException => {
          println(badarg)
          var errorList = currToken.errors :+  "Exception: " + badarg
          val newToken = currToken.copy(discourse = QuotedText,
          errors = errorList)
          for (ch <- el.child) {
            collectTokens(newToken, ch)
          }
        }
        case ex: Exception => {
          println("Unrecognized exception " + ex)
          var errorList = currToken.errors :+  "Exception: " + ex
          val newToken = currToken.copy(discourse = QuotedText,
          errors = errorList)
          for (ch <- el.child) {
            collectTokens(newToken, ch)
          }
        }
      }
    }
  }


  /** collect all tokens descended from a given XML node
  *
  * Results are collected in `tokenBuffer`.
  *
  * @param currToken token reflecting reading values for parent context
  * @param n XML node to collect content from
  */
  def collectTokens(currToken: HmtToken, n: xml.Node): Unit = {
    n match {
      case t: xml.Text => {
        val depunctuate =  t.text.split(punctuationSplitter)
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
            collectWrappedWordReadings(Clear,e)
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
            collectCited(currToken,e)
          }
          case "ref" => {}
          case "num" => {
            val newToken = currToken.copy(lexicalCategory = NumericToken, lexicalDisambiguation = CiteUrn("urn:cite:hmt:disambig.numeric.v1"))
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
            collectWrappedWordReadings(Clear,e)
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



  /** read an XML fragment following HMT conventions to represent a single
  * citable node, and construct a Vector of (CtsUrn,[[org.homermultitext.edmodel.HmtToken]]) tuples from it
  *
  * @param u URN for the citable node
  * @param xmlStr XML text for the citable node
  */
  def teiToTokens(u: CtsUrn, xmlStr: String) : Vector[TokenAnalysis]  = {
    val urnKey = u.workComponent + "_tkns"
    //  generate editionUrn CtsUrn base like  "tlg5026.msA.hmt_tkns"
    // get analysis CiteUrn from analyticalCollections map keyed to that value

    val root  = XML.loadString(xmlStr)
    val currToken = HmtToken(
      editionUrn = CtsUrn("urn:cts:greekLit:" + urnKey + ":"),
      sourceUrn = CtsUrn(u.toString + "@" + "UNSPECIFIED"),
      analysis = analyticalCollections(urnKey),
      lexicalCategory = LexicalToken,
      readings = Vector.empty
    )
    tokenBuffer.clear
    collectTokens(currToken, root)

    // in the final result, add exemplar-level
    // citation element
    val zippedVal = tokenBuffer.zipWithIndex.map{ case (t,i) => {
      val baseUrn = t.editionUrn
      t.editionUrn = CtsUrn(baseUrn.toString + "." + (i +1))
      (baseUrn, t) }
    }.toVector

    zippedVal.map{
      case (u,t) => TokenAnalysis(u,t)
    }
  }


  def fromTwoColumns(fileName: String): Vector[TokenAnalysis] = {
    fromTwoColumns(fileName,"\t")
  }

  def fromTwoColumns(fileName: String, separator: String): Vector[TokenAnalysis] = {
    val pairArray = scala.io.Source.fromFile(fileName).getLines.toVector.map(_.split(separator)).map( arr => (CtsUrn(arr(0)), arr(1)))
    pairArray.flatMap{ case (u,x) => TeiReader.teiToTokens(u,x) }
  }

}

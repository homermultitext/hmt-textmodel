package org.homermultitext.edmodel

import edu.holycross.shot.mid.validator._
import edu.holycross.shot.xmlutils._

import scala.xml._
import scala.io.Source

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._



/** An implementation of the MidMarkupReader trait for HMT project editions.
*
* @param hmtEditionType Type of edition to generate.
*/
case class TeiReader(hmtEditionType : MidEditionType) extends MidMarkupReader {


  // required by MidMarkupReader
  def editionType: MidEditionType = hmtEditionType


  // required by MidMarkupReader
  def recognizedTypes: Vector[MidEditionType] =  TeiReader.editionTypes


  // required by MidMarkupReader
  def editedNode(cn: CitableNode): CitableNode = {
    hmtEditionType match {
      case HmtNamedEntityEdition => NamedEntityReader.neNode(cn)
      case _ => throw new Exception("Don't yet know how to make an edition of type " + hmtEditionType)
    }
  }
}

/** Object for parsing TEI XML into the HMT project object model of an edition. */
object TeiReader {


  /** Vector of MidEditionTypes that this object can produce.
  */
  def editionTypes:  Vector[MidEditionType] =  Vector(
    HmtNamedEntityEdition,
    HmtDiplomaticEdition,
    HmtScribalNormalizedEdition,
    HmtEditorsNormalizedEdition
  )

  /** URL encode any colon characters in s so that s
  * can be used as the extended citation string of a CtsUrn.
  *
  * @param s String to use as extended citation string of a CtsUrn.
  */
  def ctsSafe(s: String): String = {
    val colon = java.net.URLEncoder.encode(":", "utf-8")
    s.replaceAll(":", colon)
  }



  def rightMost(s: String, subString: String) : String = {
    s.slice(s.indexOf(subString) + 1, s.size)
  }


  // inclusive
  def leftMost(s: String, subString: String) : String = {
    s.slice(0,s.indexOf(subString) + subString.size + 1)
  }



  /** Create an [[HmtToken]] for a single String.
  *
  * @param tknString The textual reading for this token.
  * @param textContext Textual context within which we need to index occurrences
  * of tknString.
  * @param settings Contextual values within the document for this token.
  */
  def tokenForString(tknString: String, settings: TokenSettings) : HmtToken = {
    val subref = ctsSafe(tknString)
    println("TOKEN FOR STRING: " + subref)
    val subrefUrn = CtsUrn(settings.contextUrn.toString + "@" + subref)

    val version = settings.contextUrn.version + "_lextokens"
    val tokenUrn = settings.contextUrn.addVersion(version)

    val lexicalCat = if (punctuation.contains(tknString)) {
      Punctuation
    } else {
      settings.lexicalCategory
    }
    val rdgs = Vector(Reading(tknString, settings.status))

    val hmtToken = HmtToken(
      sourceUrn = subrefUrn,
      editionUrn = tokenUrn,
      readings = rdgs,
      lexicalCategory = lexicalCat,
      lexicalDisambiguation =  Cite2Urn("urn:cite2:hmt:disambig.v1:lexical"),
      alternateReading = None,
      discourse = settings.discourse,
      externalSource = settings.externalSource,
      errors = settings.errors
    )
    hmtToken
  }

  /** Intimidating regular expression splitting strings by HMT Greek punctuation. */
  val punctuationSplitter = "((?<=[,;:⁑\\.])|(?=[,;:⁑\\.]))"

  /** Extract tokens from a TEI text node's string value.
  *
  * @param str String to tokenize.
  * @param settings State of text at this point.
  */
  def tokensFromText(str: String, settings: TokenSettings) : Vector[HmtToken] = {
    val hmtText = HmtChars.hmtNormalize(str)
    val depunctuate =  hmtText.split(punctuationSplitter).toVector

    val tokenStrings = depunctuate.flatMap(_.split("[ ]+")).filter(_.nonEmpty).toVector

    val hmtTokens = for (tknString <- tokenStrings) yield {
      val t = tokenForString(tknString, settings)
      t
    }
    hmtTokens.toVector
  }



  def collectWrappedTokenReadings(n: xml.Node, status: EditorialStatus):  Vector[Reading]  = {
    val rdgs = n match {

      case t: xml.Text => {
        val readingString = t.text.replaceAll(" ", "")
        if (readingString.nonEmpty) {
          val sanitized = HmtChars.hmtNormalize(readingString)
          Vector(Reading(sanitized, status))
        } else {
          Vector.empty[Reading]
        }
      }

      case e: xml.Elem => {
        e.label match {
          case "unclear" => {
            Vector(Reading(TextReader.collectText(e), Unclear))
          }
          case _ => {
            // RECORD ERROR
            println("DON'T KNOW ABOUT " + n)
            Vector.empty[Reading]
          }
        }
      }

      case _ =>
      Vector.empty[Reading]
    }
    rdgs.toVector
  }


  /** Check conformance of an element with HMT markup requirements,
  * and create a new settings object for parsing by adding any
  * further error messages to the current settings object.
  *
  * @param el Element to analyze.
  * @param settings Current [[TokenSettings]].
  */
  def hierarchySettings(el: scala.xml.Elem, settings: TokenSettings): TokenSettings = {
    HmtTeiElements.tier(el.label) match {
      case None => settings
      case tierOpt: Option[HmtTeiTier] => {
        val tier = tierOpt.get
        //println("AT " + el.label + " with allowed children " + tier.allowedChildren)

        val childMsgs = for (ch <- el.child) yield {
          if (
            (ch.label == "#PCDATA") || (tier.allowedChildren.contains(ch.label))
          ) {
            ""
          } else {
            "Elements out of place in markup hierarchy. '" + el.label + "' may not contain child element '" + ch.label + "'"

          }
        }
        val msgs = HmtTeiElements.tierDepth(el.label) match {
          case None => childMsgs.toVector
          case i : Option[Int] => {
            if (settings.treeDepth >= i.get) {
              childMsgs.toVector
            } else {
              val msg = s"Element ${el.label} out of place in markup hierarchy."
              childMsgs.toVector :+ msg
            }
          }
        }
        settings.addErrors(msgs.filter(_.nonEmpty))
      }
    }
  }

  /** Extract tokens from a TEI element.
  *
  * @param el Element to tokenize.
  * @param settings State of text at this point.
  */
  def tokensFromElement(el: scala.xml.Elem, settings: TokenSettings) : Vector[HmtToken] = {

    if (HmtTeiElements.metadata.contains(el.label)) {
      Vector.empty[HmtToken]

    } else if (HmtTeiElements.structural.contains(el.label)) {
      val tkns = for (ch <- el.child) yield {
        collectTokens(ch, settings)
      }
      tkns.toVector.flatten


      } else {
        val depthSettings = hierarchySettings(el, settings)
        val settingsWithAttrs = HmtTeiAttributes.errorMsg(el) match {
          case None => {
            depthSettings
          }
          case err: Option[String] => {
            depthSettings.addError(err.get)
          }
        }
        el.label match {

        // Level 1:  reading status the is innermost markup, so if it
        // occurs alone, we can directly collect text from here.
        case "unclear" => {
          tokensFromText(el.text, settingsWithAttrs)
        }

        // Level 2:  these editorial status elements can wrap a TEI "unclear" or "gap"
        case "add" => {
          Vector.empty[HmtToken]

          /*
          //  multiform?  Or correction?
          wrappedWordBuffer.clear
          collectWrappedWordReadings(Clear,el)
          val alt = AlternateReading(Multiform,wrappedWordBuffer.toVector)
          wrappedWordBuffer.clear
          val newToken = tokenSettings.copy(alternateReading = Some(alt), readings = wrappedWordBuffer.toVector)
          wrappedWordBuffer.clear
          tokenBuffer += newToken
          */
        }


        // Level 2:  tokenizing elements.  Build a single token directly from these.
        case "num" => {
          // Text for token, and associated vector of readings:
          val txt = TextReader.collectText(el)
          val allReadings = for (ch <- el.child) yield {
            collectWrappedTokenReadings(ch, settingsWithAttrs.status)
          }
          val subrefUrn = CtsUrn(settingsWithAttrs.contextUrn.toString + "@" + txt)

          val version = settingsWithAttrs.contextUrn.version + "_lextokens"
          val tokenUrn = settingsWithAttrs.contextUrn.addVersion(version)
          Vector(
            HmtToken(
              sourceUrn = settingsWithAttrs.contextUrn,
              editionUrn = tokenUrn,
              lexicalCategory = NumericToken ,
              readings = allReadings.toVector.flatten,
              errors = settingsWithAttrs.errors
            )
          )
        }


        case bad: String =>  {
          var error = "Element " + bad + " not allowed or not yet implemented in textmodel library."
          val newSettings = settingsWithAttrs.addError(error)
              val tkns = for (ch <- el.child) yield {
            collectTokens(ch, newSettings)
          }
          val flattened = tkns.toVector.flatten
          flattened
        }

      }
    }
  }


  def collectTokens(n: xml.Node, settings: TokenSettings): Vector[HmtToken] = {
    n match {
      case t: xml.Text => {
        val sanitized = HmtChars.hmtNormalize(t.text).trim
        if (sanitized.isEmpty) {
          Vector.empty[HmtToken]
        } else {
          tokensFromText(sanitized, settings)
        }
      }
      case e: xml.Elem => {
        tokensFromElement(e, settings)
      }
    }
  }

  /** Extract tokens from the root of citable node represented as an XML node,
  * which can be either an Element or a Text node.
  * First walk the XML tree for basic tokenization.  Then
  * cycle through the resulting list of
  * tokens to add a token level to the URN's passage component.
  *
  * @param n Node to tokenize.
  * @param settings State of text at this point.
  */
  def collectCitableTokens(n: xml.Node, settings: TokenSettings): Vector[HmtToken] = {
    //1. Walk XML tree.
    val rawTokens = n match {
      case t: xml.Text => {
        val sanitized = HmtChars.hmtNormalize(t.text)
        tokensFromText(sanitized, settings)
      }
      case e: xml.Elem => {
        tokensFromElement(e, settings)
      }
    }

    // 2. Index to subreference strings.
    val fullString = rawTokens.map(_.readWithDiplomatic).mkString("")
    val accumulated = StringBuilder.newBuilder
    val citableTokens = for ((tkn,count) <- rawTokens.zipWithIndex) yield {
      accumulated.append(tkn.readWithDiplomatic)
      val idx = tkn.readWithDiplomatic.r.findAllMatchIn(accumulated.toString).length
      val subref =  "@" + tkn.readWithDiplomatic + "[" + idx + "]"
      val tknUrn = CtsUrn(tkn.editionUrn.toString + "." + count + subref)

      tkn.adjustEditionUrn(tknUrn)
    }

    citableTokens
  }


  /** Analyze a single citable node into a Vector of [[HmtToken]]s.
  *
  *@param n Node to analyze.
  */
  def analyzeCitableNode(cn: CitableNode): Vector[HmtToken] = {
    val settings =TokenSettings(cn.urn)
    val xml = XML.loadString(cn.text)
    collectCitableTokens(xml,settings)
  }


  /** Analyze an OHCO2 corpus.  Creates a Vector of
  * [[HmtToken]]s representing every token in the TEI source.
  *
  * @param c Corpus to analyze.
  */
  def analyzeCorpus(c: Corpus): Vector[HmtToken] = {
    val tokens = for (cn <- c.nodes) yield {
      analyzeCitableNode(cn)
    }
    tokens.flatten
  }

}

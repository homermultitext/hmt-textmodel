package org.homermultitext.edmodel


import edu.holycross.shot.citevalidator._
import edu.holycross.shot.mid.markupreader._
import edu.holycross.shot.mid.orthography._
import edu.holycross.shot.xmlutils._

import scala.xml._
import scala.io.Source

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._


import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


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
  def editedNode(cn: CitableNode, hmtEditionType: MidEditionType): CitableNode = {
    hmtEditionType match {
      case HmtDiplomaticEdition => DiplomaticReader.editedNode(cn)
      case HmtScribalNormalizedEdition => ScriballyNormalizedReader.editedNode(cn)
      case HmtEditorsNormalizedEdition => EditoriallyNormalizedReader.editedNode(cn)

      case _ => throw new Exception("Don't yet know how to make an edition of type " + hmtEditionType)
    }
  }
}

/** Object for parsing TEI XML into the HMT project object model of an edition. */
object TeiReader extends LogSupport {
   Logger.setDefaultLogLevel(LogLevel.DEBUG)
  /** Vector of MidEditionTypes that this object can produce.
  */
  def editionTypes:  Vector[MidEditionType] =  Vector(
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



  /** Create an [[HmtToken]] for a single String in a context
  * described by a TokenSettings object when you reach the leaf of
  * of your XML tree.
  *
  * @param tknString The textual reading for this token.
  * @param textContext Textual context within which we need to index occurrences
  * of tknString.
  * @param settings Contextual values within the document for this token.
  */
  def tokenForString(tknString: String, settings: TokenSettings) : HmtToken = {
    val subref = ctsSafe(tknString)
    val subrefUrn = CtsUrn(settings.contextUrn.toString + "@" + subref.replaceAll("\n",""))

    val version = settings.contextUrn.version + "_lextokens"
    val tokenUrn = settings.contextUrn.addVersion(version)

    val lexicalCat = if (punctuation.contains(tknString)) {
      HmtPunctuationToken
    } else {
      settings.lexicalCategory
    }
    val rdgs = settings.alternateCategory match {
      case None => Vector(Reading(tknString, settings.status))
      case alt: Option[AlternateCategory] => {
        alt.get match {
          case Multiform =>  Vector.empty[Reading]
          case Correction =>  Vector.empty[Reading]
          case Restoration =>  Vector.empty[Reading]

          case Deletion => Vector(Reading(tknString, settings.status))
        }
      }
    }
    val altRdg : Option[AlternateReading] = settings.alternateCategory match {
      case None => None
      case alt: Option[AlternateCategory] => {
        alt.get match {
          case Multiform =>   Some(AlternateReading(alt.get, Vector(Reading(tknString, settings.status))))
          case Correction => Some(AlternateReading(alt.get, Vector(Reading(tknString, settings.status))))
          case Restoration => {
            debug("WOOHOO!  Expanding abbrs!")
            Some(AlternateReading(alt.get, Vector(Reading(tknString, settings.status))))
          }
          case Deletion => Some(AlternateReading(alt.get, Vector.empty[Reading]))
        }
      }
    }
    val hmtToken = HmtToken(
      sourceUrn = subrefUrn,
      editionUrn = tokenUrn,
      readings = rdgs,
      lexicalCategory = lexicalCat,
      lexicalDisambiguation =  settings.lexicalDisambiguation,
      alternateReading = altRdg,
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

  // recursively gather readings from a tokenized element.
  def collectWrappedTokenReadings(n: xml.Node, settings: TokenSettings, readings: Vector[Reading] = Vector.empty[Reading]):  Vector[Reading]  = {
    val rdgs = n match {
      case t: xml.Text => {
        val readingString = t.text.replaceAll(" ", "")
        if (readingString.nonEmpty) {
          val sanitized = HmtChars.hmtNormalize(readingString)
          readings :+ Reading(sanitized, settings.status)
        } else {
          Vector.empty[Reading]
        }
      }

      case e: xml.Elem => {
        if (
          (TokenizingElements.allowedElements.contains(e.label)) ||
          (TokenizingElements.allowedChildren.contains(e.label))
        ) {
            val allReadings = for (ch <- e.child) yield {
              collectWrappedTokenReadings(ch, settings, readings)
            }
            allReadings.flatten
        } else {
          val errMsg = "Illegal element " + e.label + " inside tokenizing element."
          val allReadings = for (ch <- e.child) yield {
            collectWrappedTokenReadings(ch, settings.addError(errMsg), readings)
          }
          allReadings.flatten
        }
      }
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
        debug("AT " + el.label + " with allowed children " + tier.allowedChildren)

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


  def wrappedToken(el: scala.xml.Elem, settings: TokenSettings, tokenType: LexicalCategory): Vector[HmtToken] = {
    // Gather all text for token URN
    val txt = TextReader.collectText(el).replaceAll("\n","")
    val subrefUrn = CtsUrn(settings.contextUrn.toString + "@" + txt)
    val version = settings.contextUrn.version + "_lextokens"
    val tokenUrn = settings.contextUrn.addVersion(version)
    // Get vector of readings:
    val allReadings = for (ch <- el.child) yield {
      collectWrappedTokenReadings(ch, settings)
    }
    // Because this is a wrapping element, we want a vector containing a
    // single token.
    Vector(
      HmtToken(
        sourceUrn = settings.contextUrn,
        editionUrn = tokenUrn,
        readings = allReadings.toVector.flatten,
        lexicalCategory = tokenType,
        lexicalDisambiguation = settings.lexicalDisambiguation,
        discourse = settings.discourse,
        externalSource = settings.externalSource,
        errors = settings.errors
      )
    )
  }



  //level 1
  def editorialTokens(el: scala.xml.Elem, settings: TokenSettings) : Vector[HmtToken] = {
    el.label match {
      // Reading status  is innermost markup, so if it
      // occurs alone, we can directly collect text from here.
      case "unclear" => {
        tokensFromText(el.text, settings.setStatus(Unclear))
      }
      case "sic" => {
        tokensFromText(el.text, settings.setStatus(Sic))
      }

      case "gap" => {
        val version = settings.contextUrn.version + "_lextokens"
        val tokenUrn = settings.contextUrn.addVersion(version)
        Vector(HmtToken(
          sourceUrn = settings.contextUrn,
          editionUrn = tokenUrn,
          lang = "",
          readings = Vector.empty[Reading],
          lexicalCategory = HmtLacuna ,

          errors = settings.errors :+ "Lacuna in text: no tokens legible")
          )
      }
    }
  }



  def scribalTokens(el: scala.xml.Elem, settings: TokenSettings) : Vector[HmtToken] = {
    el.label match {

      // Level 3:  editorial status elements
      case "add" => {
        val tkns = for (ch <- el.child) yield {
          collectTokens(ch, settings.addAlternateCategory(Some(Multiform)))
        }
        tkns.toVector.flatten
      }
      case "del" => {
        val tkns = for (ch <- el.child) yield {
          collectTokens(ch, settings.addAlternateCategory(Some(Deletion)))
        }
        tkns.toVector.flatten
      }
      case "choice" => {
        val childElems = el.child.toVector.filterNot(_.label == " #PCDATA")
        debug("NUMBER NON-PCDATA CHILDREN: " + childElems.size)
        // enforce correct pairings
        if ((childElems.size == 2) && (HmtTeiChoice.validChoice(el))) {
          val children = el.child.toVector
          val t1 = collectTokens(children(0), settings)
          val t2 = collectTokens(children(1), settings)
          HmtTeiChoice.pairedToken(t1,t2,settings)

        } else {
          val msg = "Illegal combination of elements within choice: " + HmtTeiChoice.choiceChildren(el).mkString(", ")
          val tkns = for (ch <- el.child) yield {
            collectTokens(ch, settings.addError(msg))
          }
          tkns.toVector.flatten
        }
      }


      /// abbr/expan pair
      case "abbr" => {
        val tkns = for (ch <- el.child) yield {
          collectTokens(ch, settings)
        }
        tkns.toVector.flatten
      }
      case "expan" => {
        val tkns = for (ch <- el.child) yield {
          collectTokens(ch, settings.addAlternateCategory(Some(Restoration)))
        }
        tkns.toVector.flatten
      }

      // corr pairs with sic, which can also appear independently.
      /// include approrpiate setting on alt. reading member of pair:
      case "corr" => {
        val tkns = for (ch <- el.child) yield {
          collectTokens(ch, settings.addAlternateCategory(Some(Correction)))
        }
        tkns.toVector.flatten
      }

      //// orig/reg pair
      case "orig" => {
        val tkns = for (ch <- el.child) yield {
          collectTokens(ch, settings)
        }
        tkns.toVector.flatten
      }
      case "reg" => {
        val tkns = for (ch <- el.child) yield {
          collectTokens(ch, settings.addAlternateCategory(Some(Multiform)))
        }
        tkns.toVector.flatten
      }
    }
  }

  def citeUrnFromAttr(el: scala.xml.Elem, attr: String = "@n"): Option[Cite2Urn] = {
    val att = el \ attr
    try {
      val u = Cite2Urn(att.text)
      Some(u)
    } catch {
      case t: Throwable => None
    }
  }


  def citeDisambiguation(el: scala.xml.Elem, settings: TokenSettings, attr: String = "@n" ) : Vector[HmtToken] = {

      citeUrnFromAttr(el,"@n")  match {
        case None => {
          val tkns = for (ch <- el.child) yield {
            collectTokens(ch, settings)
          }
          tkns.toVector.flatten
        }
        case u: Option[Cite2Urn] => {
          val settingWithId = settings.addDisambiguation(u.get)
          val tkns = for (ch <- el.child) yield {
            collectTokens(ch, settingWithId)
          }
          tkns.toVector.flatten
        }
    }
  }
  def disambiguatingTokens(el: scala.xml.Elem, settings: TokenSettings) : Vector[HmtToken] = {

    el.label match {
      case "persName" => citeDisambiguation(el,settings)
      case "placeName" =>  citeDisambiguation(el,settings)
      case "title" => citeDisambiguation(el,settings)

      case "rs" => {
        val rsType = (el \ "@type").text
        val rsTokens = rsType match {

          case "astro" => citeDisambiguation(el,settings)
          case "ethnic" => citeDisambiguation(el,settings)

          case "waw" => {
            val newSettings = settings.setDiscourse(QuotedLiteral)
            val tkns = tokensFromText(el.text, newSettings)
            tkns
          }

          case s: String => {
            val msg = "Type attribute value '" + (el \ "@type").text + "' on rs element not recognized."
            val newSettings  = settings.addError(msg)
            val tkns = tokensFromText(el.text, newSettings)
            tkns
          }
        }
        rsTokens
      }
    }
  }


  def discourseTokens(el: scala.xml.Elem, settings: TokenSettings) : Vector[HmtToken] = {
    el.label match {
      case "q" => {
        val newSettings = settings.setDiscourse(QuotedLanguage)
        val tkns = tokensFromText(el.text, newSettings)
        tkns
      }

      case s: String =>{
        val msg = "Elem '" + el.label + "' not recognized."
        val newSettings  = settings.addError(msg)
        val tkns = tokensFromText(el.text, newSettings)
        tkns
      }
    }
  }


  /** Extract tokens from a TEI element.
  *
  * @param el Element to tokenize.
  * @param settings State of text at this point.
  */
  def tokensFromElement(el: scala.xml.Elem, settings: TokenSettings) : Vector[HmtToken] = {

    def finalTokens = if (HmtTeiElements.metadata.contains(el.label)) {
      Vector.empty[HmtToken]

    } else if (HmtTeiElements.structural.contains(el.label)) {
      val tkns = for (ch <- el.child) yield {
        collectTokens(ch, settings)
      }
      tkns.toVector.flatten

      // Content elements we have to process:
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

      if (EditorReading.allowedElements.contains(el.label)) {
        editorialTokens(el, settingsWithAttrs)

      } else if (TokenizingElements.allowedElements.contains(el.label)) {
        // Level 2:  tokenizing elements.  Build a single token directly from these.
        el.label match {
          case "num" => wrappedToken(el, settingsWithAttrs, HmtNumericToken)
          case "w" => wrappedToken(el, settingsWithAttrs, HmtLexicalToken)
          case "foreign" => wrappedToken(el, settingsWithAttrs, HmtLexicalToken)
        }

      } else if (
        (AllScribalReading.allowedElements.contains(el.label)) ||
        (el.label == "choice")
        ){
          scribalTokens(el, settingsWithAttrs)

      } else if (DisambiguatingElements.allowedElements.contains(el.label)) {
        disambiguatingTokens(el,settingsWithAttrs)


      } else if (el.label == "ref") {
        // should not be used independently:
        val msg = s"In ${settings.contextUrn}, illegal use of TEI <ref> outside of <cit>"
        val newSettings = settings.addError(msg)
        error(msg)
        Vector.empty[HmtToken]

      } else if (el.label == "cit") {
        debug("Looking at cit element: " + el)
        val newSettings = settings.setDiscourse(QuotedText)
        val childElems = el.child.toVector.filterNot(_.label == " #PCDATA")

        // MODIFY SETTINGS:  NOW DIRECT SPEECH

        //if ((childElems.size == 2) && HmtTeiCit.validCit(el)) {
        if (HmtTeiCit.validCit(el)) {
          val qval = el \ "q"
          debug("q is " + qval(0).text)
          val tkns = tokensFromText(qval(0).text, newSettings)
          debug("yielding tokens " + tkns)
          tkns

        } else {
          val msg = "Illegal child elements within cit:  " + HmtTeiCit.citChildren(el).mkString(", ")
          debug("ILLEGAL CHILDREN: " + el.child)
          val newSettings = settings.addError(msg)
          // NO!  GO DOWN TO CHILDREN
        //  val tkns = tokensFromText(el.text, newSettings)


          val tkns = for (ch <- el.child) yield {
            debug("COLLECT TOKENS FOR " + ch.label)
            //collectTokens(ch, newSettings)
          }
          //val flattened = tkns.toVector.flatten
          //flattened
          Vector.empty[HmtToken]
        }

      } else if (DiscourseAnalysis.allowedElements.contains(el.label)) {
        discourseTokens(el, settingsWithAttrs)

      } else {
        var error = "Element " + el.label + " not allowed or not yet implemented in textmodel library."
        val newSettings = settingsWithAttrs.addError(error)
            val tkns = for (ch <- el.child) yield {
          collectTokens(ch, newSettings)
        }
        val flattened = tkns.toVector.flatten
        flattened
      }
    }
    debug("FINAL TOKENS: " + finalTokens.size)
    finalTokens
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
      if (tkn.lexicalCategory == HmtLacuna)  {
        tkn
      } else {
        accumulated.append(tkn.readWithDiplomatic)
        val idx = tkn.readWithDiplomatic.r.findAllMatchIn(accumulated.toString).length
        val subref =  "@" + tkn.readWithDiplomatic + "[" + idx + "]"
        val tknUrn = CtsUrn(tkn.editionUrn.toString + "." + count + subref.replaceAll("\n",""))

        tkn.adjustEditionUrn(tknUrn)
      }
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

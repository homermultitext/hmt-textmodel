package org.homermultitext.edmodel

import edu.holycross.shot.mid.validator._


import scala.xml._
import scala.io.Source

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._


case class TeiReader(hmtEditionType : MidEditionType) extends MidMarkupReader {

  def editionType: MidEditionType = hmtEditionType


  def recognizedTypes: Vector[MidEditionType] =  TeiReader.editionTypes


  def editedNode(cn: CitableNode): CitableNode = {
    hmtEditionType match {
      case HmtNamedEntityEdition => NamedEntityReader.neNode(cn)
      case _ => throw new Exception("Don't yet know how to make an edition of type " + hmtEditionType)
    }
  }
}

object TeiReader {

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
    val unsafe = ":"
    val encoded = java.net.URLEncoder.encode(unsafe, "utf-8")
    s.replaceAll(unsafe, encoded)
  }



  def rightMost(s: String, subString: String) : String = {
    s.slice(s.indexOf(subString) + 1, s.size)
  }


  // inclusive
  def leftMost(s: String, subString: String) : String = {
    s.slice(0,s.indexOf(subString) + subString.size + 1)
  }



  def tokenForString(tknString: String, idx: Int, accumulated : String, settings: TokenSettings) : HmtToken = {
    val subref = ctsSafe(tknString)
    val subrefIndex =  tknString.r.findAllMatchIn(accumulated).length
    val subrefUrn = CtsUrn(settings.contextUrn.toString + "@" + subref + "[" + subrefIndex + "]")


    val psg = settings.contextUrn.passageComponent + "." + idx
    val version = settings.contextUrn.version + "_lextokens"
    val tokenUrn = settings.contextUrn.addVersion(version).addPassage(psg)

    val lexicalCat = if (punctuation.contains(tknString)) {
      Punctuation
    } else {
      LexicalToken
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
      externalSource = settings.externalSource
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
    val depunctuate =  hmtText.split(punctuationSplitter)
    val tokenStrings = depunctuate.flatMap(_.split("[ ]+")).filter(_.nonEmpty).toVector


    val accumulated = StringBuilder.newBuilder
    val hmtTokens = for ((tknString,idx) <- tokenStrings.zipWithIndex) yield {
      accumulated.append(tknString)
      tokenForString(tknString, idx, accumulated.toString, settings)
    }
    hmtTokens.toVector
  }


  /** Extract tokens from a TEI element.
  *
  * @param el Element to tokenize.
  * @param settings State of text at this point.
  */
  def tokensFromElement(el: scala.xml.Elem, settings: TokenSettings) : Vector[HmtToken] = {
      el.label match {
      // Level 0:  omit
      case "note" =>   Vector.empty[HmtToken] // to be removed from archive
      case "figDesc" =>   Vector.empty[HmtToken] // metadata, don't process
      case "ref" =>   Vector.empty[HmtToken] // metadata, don't process
      case _ => throw new Exception("TeiReader.tokensFromElement: do not recognize element " + el.label)
    }

  }



  /** Extract tokens from an XML node, which can be either an Element
  * or a Text node.
  *
  * @param n Node to tokenize.
  * @param settings State of text at this point.
  */
  def collectTokens(n: xml.Node, settings: TokenSettings): Vector[HmtToken] = {
    n match {
      case t: xml.Text => {
        val sanitized = HmtChars.hmtNormalize(t.text)
        tokensFromText(sanitized, settings)
      }
      case e: xml.Elem => {
        tokensFromElement(e, settings)
      }
    }
  }

}

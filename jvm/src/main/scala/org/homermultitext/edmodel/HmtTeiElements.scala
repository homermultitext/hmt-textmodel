package org.homermultitext.edmodel

import scala.xml._


import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


/** Trait defining HMT usage of TEI markup allowed at different tiers
* in a document.*/
trait HmtTeiTier  {
  /** Elements permitted at this tier.*/
  def allowedElements : Set[String]
  /** Children permitted at this tier.  In some cases,
  * TEI schemas will further limit which child elements
  * are allowed for which parent element.*/
  def allowedChildren: Set[String]
}

/** Lowest tier:  HMT editor's ability to read the text.
* Should contain no child elements: only text nodes.
* The default state is "clear" readings, and requires no
* markup to indicate that.*/
case object EditorReading extends HmtTeiTier {
  def allowedElements = Set("unclear", "gap", "sic")
  def allowedChildren = Set()
}



/** Second-lowest tier:  elements grouping contents into tokens
* of a particular type.  The default token types are white-space
* delimited lexical tokens and punctuation tokens identied by
* individual character value.
* The TEI w element is used to group lexical tokens that are
* broken into separate nodes by other markup  */
case object TokenizingElements extends HmtTeiTier {
  def allowedElements = Set("foreign", "num", "w")
  def allowedChildren = AllScribalReading.allowedElements ++ AllScribalReading.allowedChildren
}

/** Third-lowest  tier: elements that can be used independently
* to identiy scribal modfications. */
case object ScribalReading extends HmtTeiTier {
  def allowedElements = Set("add", "del")
  def allowedChildren = EditorReading.allowedElements
}

/** Third-lowest tier:  elements that can be used within a grouping
* TEI choice element  These include further categories of scribal modifications.
* plus editorial expansion of abbreviations.*/
case object PairedScribalReading extends HmtTeiTier {
  def allowedElements = Set("sic", "corr", "abbr", "expan", "orig","reg")
  def allowedChildren = EditorReading.allowedElements ++ EditorReading.allowedChildren
}

/** All elements belonging to the third tier of HMT markup.*/
case object AllScribalReading extends HmtTeiTier {
  def allowedElements = ScribalReading.allowedElements ++ PairedScribalReading.allowedElements
  def allowedChildren = EditorReading.allowedElements ++ EditorReading.allowedChildren
}




/** Fourth-lowest tier: elements disambiguating named entities.
*/
case object DisambiguatingElements extends HmtTeiTier {
  def allowedElements = Set("persName", "placeName", "rs", "title")
  def allowedChildren = TokenizingElements.allowedElements ++ TokenizingElements.allowedChildren
}

/**
*/
case object DiscourseAnalysis extends HmtTeiTier {
    def allowedElements = Set("q","ref")
    def allowedChildren = DisambiguatingElements.allowedElements ++ DisambiguatingElements.allowedChildren
}

/** Static definition of TEI elements allowed in HMT editions,
* and specification of their permitted hierarchical relations. */
object HmtTeiElements {

  /** Elements defining document structure. */
  val structural = Set(
    "div", "l", "p", "list", "item", "floatingText", "figure", "body"
  )
  /** Metadata elements.*/
  val metadata = Set("figDesc", "note","ref")


  /** Hierarchical order of tiers of markup.*/
  val tiers: Vector[HmtTeiTier] = Vector(
    EditorReading,
    TokenizingElements,
    AllScribalReading,
    DisambiguatingElements,
    DiscourseAnalysis
  )

  /** Elements containing tokenizable content.*/
  val contentElements = tiers(0).allowedElements ++ tiers(0).allowedChildren

  /** All elements allowed by HMT conventions.*/
  val hmtLegal = contentElements ++ metadata ++ structural

  /** Find tier an element belongs to.
  *
  * @param elName Name of element to look for.
  */
  def tier(elName: String): Option[HmtTeiTier] = {
    val matching = tiers.filter(_.allowedElements.contains(elName))
    matching.size match {
      case 0 => None
      case _ => Some(matching(0))
    }
  }

  def tierDepth(elName: String) : Option[Int] = {
    tier(elName) match {
      case None => None
      case t: Option[HmtTeiTier] => {
        Some(tiers.indexOf(t.get))
      }
    }
  }
}

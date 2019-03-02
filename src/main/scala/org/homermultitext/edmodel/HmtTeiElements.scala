package org.homermultitext.edmodel


trait HmtTeiTier {
  def allowedElements : Set[String]
  def allowedChildren: Set[String]
}

case object EditorReading extends HmtTeiTier {
  def allowedElements = Set("unclear", "gap", "sic")
  def allowedChildren = Set()
}

case object ScribalReading extends HmtTeiTier {
  def allowedElements = Set("add", "del")
  def allowedChildren = EditorReading.allowedElements
}

// Must separately check for correct pairings within `choice`
case object PairedScribalReading extends HmtTeiTier {
  def allowedElements = Set("sic", "corr", "abbr", "expan", "orig","reg")
  def allowedChildren = EditorReading.allowedElements
}

case object TokenizingElements extends HmtTeiTier {
  def allowedElements = Set("foreign", "num", "w")
  def allowedChildren = PairedScribalReading.allowedElements ++ PairedScribalReading.allowedChildren
}

case object DisambiguatingElements extends HmtTeiTier {
  def allowedElements = Set("persName", "placeName", "rs", "title")
  def allowedChildren = TokenizingElements.allowedElements ++TokenizingElements.allowedChildren
}

case object DiscourseAnalysis extends HmtTeiTier {
    def allowedElements = Set("q","cite","ref")
    def allowedChildren = DisambiguatingElements.allowedElements ++ DisambiguatingElements.allowedChildren
}

/** Static definition of TEI elements allowed in HMT editions,
* and specification of their permitted hierarchical relations. */
object HmtTeiElements {

  /** Elements defining document structure. */
  val structural = Set(
    "div", "l", "p", "list", "item", "floatingText"
  )
  /** Metadata elements.*/
  val metadata = Set("figDesc", "note")


  /** Hierarchical order of tiers of markup.*/
  val tiers: Vector[HmtTeiTier] = Vector(
    DiscourseAnalysis,
    DisambiguatingElements,
    TokenizingElements,
    PairedScribalReading,
    EditorReading
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

}

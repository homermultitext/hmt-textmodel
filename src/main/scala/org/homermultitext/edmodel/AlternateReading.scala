package org.homermultitext.edmodel

/** All possible categories for alternate readings
* are enumerated by case objects extending this trait
*
* Used by [[org.homermultitext.edmodel.AlternateReading]] and therefore also by [[org.homermultitext.edmodel.HmtToken]] and [[org.homermultitext.edmodel.TeiReader]]
*/
sealed trait AlternateCategory {def name : String}

/** Restored by modern editor, indicated in HMT XML with expan/abbr pair.*/
case object Restoration extends AlternateCategory {val name = "editorial restoration or completion"}

 /** Scribal correction of text indicated in HMT XML with sic/corr pair.*/
case object Correction extends AlternateCategory {val name = "scribal correction"}


/** Alternate reading offered by scribe  indicated in HMT XML with add. */
case object Multiform extends AlternateCategory {val name
 = "scribally recorded multiform"}

 /** Scribal deletion of text indicated by HMT del.*/
case object Deletion extends AlternateCategory {val name = "scribal deletion"}




/** an alternate reading for a token
*
* The `name` member must be implemented with an English description of the editorial status
*
* @param alternateCategory category of alternate reading
* @param reading all [[org.homermultitext.edmodel.Reading]]s for this alternate reading
*/
case class AlternateReading (alternateCategory: AlternateCategory, reading: Vector[Reading] ) {

  def leidenize: String = {
    Reading.leidenize(reading) + " (" + alternateCategory + ")"
  }
  def simpleString: String = {
    alternateCategory match {
      case Deletion => ""
      case _ => reading.map(_.text).mkString
    }

  }
}

/** string formatting function
*/
object AlternateReading {

  /** format all [[org.homermultitext.edmodel.Reading]]s in a single string*/
  def alternative (alt: AlternateReading): String = {
    alt.reading.map(rdg => rdg.typedText).mkString(" + ")
  }
}

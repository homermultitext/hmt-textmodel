package org.homermultitext.edmodel


/** All possible values for the editorial status of a token
* are enumerated by case objects extending this trait
*
* The `name` member must be implemented with an English description of the editorial status
*
* Used by [[org.homermultitext.edmodel.Reading]] and therefore also by [[org.homermultitext.edmodel.HmtToken]] and [[org.homermultitext.edmodel.TeiReader]]
*
*/
sealed trait EditorialStatus {def name : String}
/** Paleographically unambiguous reading.
*/
case object Clear extends EditorialStatus {val name = "clear"}
/** Paleographically ambiguous reading.
*/
case object Unclear extends EditorialStatus {val name = "unclear"}
/**  Lacuna.
*/
case object Missing extends EditorialStatus {val name = "missing"}
/** Reading supplied by modern editor.
*
* Applies only to editorial expansion of abbreviations.
*/
case object Restored extends EditorialStatus {val name = "restored"}

/** Reading cannot  be determined because source XML
* does not comply with HMT project requirements.
*/
case object InvalidToken extends EditorialStatus {val name = "invalid"}


case object Sic extends EditorialStatus {val name = "unintelligible"}


/** A typed reading of a passage.
*
* @constructor create a new reading with a string of text and an editorial status.
* @param reading string read with given status
* @param status status of the given string
*/
case class Reading (val text: String, val status: EditorialStatus ) {
  def typedText = text + " (" + status.name + ")"

  /**  Format text value of readings in Leiden-like string.
  */
  def leidenize: String = {
    status match {
      case Restored => "[" + text +"]"
      case Unclear => {
        val codepts = codeptList(text)
        codepts.map(_.toChar).mkString("?") + "?"
      }
      case Clear => text
      case Missing => "…"
      case InvalidToken => text
      case Sic => text
    }
  }
}

/** Companion object for formatting Vectors of [[Reading]]s as Strings.
*/
object Reading {

  def leidenize(readings: Vector[Reading]): String = {
    readings.map(_.leidenize).mkString
  }
}

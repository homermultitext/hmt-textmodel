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
/** paleographically unambiguous reading
*/
case object Clear extends EditorialStatus {val name = "clear"}
/** paleographically ambiguous reading
*/
case object Unclear extends EditorialStatus {val name = "unclear"}
/** reading supplied by modern editor
*
* Applies only to editorial expansion of abbreviations.
*
*/
case object Restored extends EditorialStatus {val name = "restored"}




/** A typed reading of a passage.
*
* @constructor create a new reading with a string of text and an editorial status.
* @param reading string read with given status
* @param status status of the given string
*/
case class Reading (val reading: String, val status: EditorialStatus ) {
  def typedText = reading + " (" + status.name + ")"

  def leidenize = {
    status match {
      case Restored => "[" + reading + "]"
      case Unclear => {
        val codepts = codeptList(reading)
        codepts.map(_.toChar).mkString("?") + "?"
      }
      case Clear => reading
    }
  }
}
/*
object Reading {
  def typedText(rdg: Reading) = rdg.reading + " (" + rdg.status.name + ")"

  def leidenize(rdg: Reading) = {
    rdg.status match {
      case Restored => "[" + rdg.reading + "]"
      case Unclear => {
        val codepts = codeptList(rdg.reading)
        codepts.map(_.toChar).mkString("?") + "?"
      }
      case Clear => rdg.reading
    }
  }
}*/

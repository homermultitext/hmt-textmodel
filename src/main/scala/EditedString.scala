package org.homermultitext.edmodel

sealed trait EditorialStatus {def name : String}
case object Clear extends EditorialStatus {val name = "clear"}
case object Unclear extends EditorialStatus {val name = "unclear"}
case object Restored extends EditorialStatus {val name = "restored"}





case class EditedString (
  val reading: String,
  val status: EditorialStatus
)
object EditedString {
  def typedText(es: EditedString) = es.reading + " (" + es.status + ")"
}

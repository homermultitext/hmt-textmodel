package org.homermultitext.edmodel

sealed trait EditorialStatus {def name : String}
case object Clear extends EditorialStatus {val name = "clear"}
case object Unclear extends EditorialStatus {val name = "unclear"}
case object Restored extends EditorialStatus {val name = "restored"}





case class Reading (
  val reading: String,
  val status: EditorialStatus
)
object Reading {
  def typedText(rdg: Reading) = rdg.reading + " (" + rdg.status.name + ")"
}

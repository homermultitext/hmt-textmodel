package org.homermultitext.edmodel
import edu.holycross.shot.mid.validator._


// Implementations of the [[MidEditionType]] trait.
/**  */
case object HmtNamedEntityEdition extends MidEditionType {
  def label =  "named entities"
  def description = "Tokenization all tokens to Option[Cite2Urn] of named entities"
  def versionId = "ne"
}

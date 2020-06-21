package org.homermultitext.edmodel
import edu.holycross.shot.mid.markupreader._


// Implementations of the [[MidEditionType]] trait for Homer Mulitext project editions.


/**
case object HmtNamedEntityEdition extends MidEditionType {
  def label =  "named entities"
  def description = "Tokenization all tokens to Option[Cite2Urn] of named entities"
  def versionId = "hmt_ne"
}
*/

case object HmtDiplomaticEdition extends MidEditionType {
  def label =  "diplomatic edition"
  def description = "Strict diplomatic version of original text"
  def versionExtension = ""
}


case object HmtScribalNormalizedEdition extends MidEditionType {
  def label =  "scribal edition"
  def description = "Diplomatic edition including scribal corrections and normalizations"
  def versionExtension = "hmt_scribal"
}

case object HmtEditorsNormalizedEdition extends MidEditionType {
  def label =  "editorially normalized edition"
  def description = "Edition normalized to modern orthographic standards."
  def versionExtension = "hmt_normalized"
  
}

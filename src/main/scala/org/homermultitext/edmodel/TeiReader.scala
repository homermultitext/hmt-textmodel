package org.homermultitext.edmodel

import edu.holycross.shot.mid.validator._

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map
import scala.xml._
import scala.io.Source

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._


/**  Factory for Vectors of  [[HmtToken]] instances.

*  ==Example==
*  The [[TeiReader]] reads data in the OHCO2
*  model from sources such as delimited-texts files or
*  the Corpus object from the edu.holycross.ohco2 library.
*  It produces a Vector of [[TokenAnalysis]] objects.
*
* Example:
*  {{{
*  val tokenPairs = TeiReader.fromCorpus(CORPUS_OBJECT)
*  }}}
*
*  ==How it works==
*  The [[TeiReader]] object maintains three mutable buffers,
*  `nodeText` (a StringBuilder), `wrappedWordBuffer` and `tokenBuffer`
* (both mutable ArrayBuffers).
*
*
*/



/// refactor this
case class TeiReader(twoColumns: String, delimiter: String = "#") extends MidMarkupReader {

  def editionType: MidEditionType = MidDiplomaticEdition


  def editionTypes: Vector[MidEditionType] =  TeiReader.editionTypes


  def editedNode(archival: String,srcUrn: edu.holycross.shot.cite.CtsUrn): String = {
    ""
  }



}

object TeiReader {

  def editionTypes:  Vector[MidEditionType] =
      Vector(
        HmtNamedEntityEdition,
        HmtDiplomaticEdition,
        HmtScribalNormalizedEdition,
        HmtEditorsNormalizedEdition
      )

}

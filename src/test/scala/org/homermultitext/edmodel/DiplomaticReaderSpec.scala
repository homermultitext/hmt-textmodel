package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class DiplomaticReaderSpec2 extends FlatSpec {


  val cexSource= "src/test/resources/archival_iliad_xml.cex"

  "The DiplomaticReader object" should "read diplomatically" in {
    val corpus = CorpusSource.fromFile(cexSource)
    val dipl = DiplomaticReader.edition(corpus)
  }
}

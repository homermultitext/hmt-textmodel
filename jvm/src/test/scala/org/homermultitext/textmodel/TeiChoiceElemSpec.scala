package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiChoiceElemSpec extends FlatSpec {


  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")

  // Status of scribe's reading:
  "The TeiReader object" should "recognize paired abbr/expan" in {
    val abbrExpan = "<p>The <choice><abbr>US of A</abbr><expan>United States of America</expan></choice> Big.</p>"
    val n = XML.loadString(abbrExpan)
    val settings = TokenSettings(context, LexicalToken)
    val pairings = TeiReader.collectTokens(n, settings)
    val paired = pairings(1)
    assert(paired.alternateReading.get.alternateCategory == Restoration)
    assert(paired.alternateReading.get.readings.head.text == "United")
    assert(paired.readings.head.text == "US")
  }

  it should "recognize abbr/expan in any order" in {
      val abbrExpan = "<p>The <choice><expan>United States of America</expan><abbr>US of A</abbr></choice> Big.</p>"
      val n = XML.loadString(abbrExpan)
      val settings = TokenSettings(context, LexicalToken)
      val pairings = TeiReader.collectTokens(n, settings)
      val paired = pairings(1)
      assert(paired.alternateReading.get.alternateCategory == Restoration)
      assert(paired.alternateReading.get.readings.head.text == "United")
      assert(paired.readings.head.text == "US")
  }




  it should "recognize a paired sic/corr" in pending /*{
    val sicCorr = "<p><choice><sic>oops</sic><corr>better</corr></choice> answer.</p>"
    val n = XML.loadString(sicCorr)
    val settings = TokenSettings(context, LexicalToken)
    val paired = TeiReader.collectTokens(n, settings).head

    assert(paired.alternateReading.get.alternateCategory == Correction)
    assert(paired.alternateReading.get.readings.head.text == "better")
    assert(paired.readings.head.text == "oops")
  }*/
  it should "recognized paired orig/reg" in pending /*{
    val origReg = "<p><choice><orig>Achilles</orig><reg>Akhilleus</reg></choice> answer.</p>"
    val n = XML.loadString(origReg)
    val settings = TokenSettings(context, LexicalToken)
    val paired = TeiReader.collectTokens(n, settings).head

    assert(paired.alternateReading.get.alternateCategory == Multiform)
    assert(paired.alternateReading.get.readings.head.text == "Akhilleus")
    assert(paired.readings.head.text == "Achilles")
  }*/

}

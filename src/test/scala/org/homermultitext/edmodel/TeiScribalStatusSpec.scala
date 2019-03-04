package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiScribalStatusSpec extends FlatSpec {


  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")

  // Status of scribe's reading:
  "The TeiReader object" should "recognize independent occurrences of TEI add" in {
    val addElem = "<p>Left <add>out</add> a word.</p>"
    val n = XML.loadString(addElem)
    val settings = TokenSettings(context, LexicalToken)
    val addTokens = TeiReader.collectTokens(n, settings)

    val addedToken = addTokens(1)
    assert(addedToken.readings.isEmpty)
    val alt = addedToken.alternateReading.get
    val expectedCategory = Multiform
    assert(alt.alternateCategory == expectedCategory)
    assert(alt.readings.head.text == "out")
    assert(alt.readings.head.status == Clear)
  }



  it should "recognize independent occurrences of TEI del" in {
    val delElem = "<p>Added too <del>too</del> many words.</p>"
    val n = XML.loadString(delElem)
    val settings = TokenSettings(context, LexicalToken)
    val delTokens = TeiReader.collectTokens(n, settings)

    val deletedToken = delTokens(2)
    val rdg = deletedToken.readings.head
    assert(rdg.text == "too")
    val alt = deletedToken.alternateReading.get
    assert(alt.alternateCategory == Deletion)
    assert(alt.readings.isEmpty)
    }


  it should "recognized paired abbr/expan" in {
    val abbrExpan = "<p><choice><abbr>Mr</abbr><expan>Mister</expan></choice> Big.</p>"
    val n = XML.loadString(abbrExpan)
    val settings = TokenSettings(context, LexicalToken)
    val paired = TeiReader.collectTokens(n, settings).head

    assert(paired.alternateReading.get.alternateCategory == Restoration)
    assert(paired.alternateReading.get.readings.head.text == "Mister")
    assert(paired.readings.head.text == "Mr")
  }

  it should "note errors in choice pairings" in {
    // this is valid TEI:
    val badCombo = "<p><choice><orig>Mister</orig><abbr>Mr</abbr></choice></p>"
    val n = XML.loadString(badCombo)
    val settings = TokenSettings(context, LexicalToken)
    val badPairing = TeiReader.collectTokens(n, settings)
    // this will prodcue two tokens, since we don't know how to join them:
    assert(badPairing.size == 2)
    val expected = "Illegal combination of elements within choice: orig, abbr"
    assert(badPairing(0).errors.head == expected)
    assert(badPairing(1).errors.head == expected)
  }


  it should "recognized paired sic/corr" in {
    val sicCorr = "<p><choice><sic>oops</sic><corr>better</corr></choice> answer.</p>"
    val n = XML.loadString(sicCorr)
    val settings = TokenSettings(context, LexicalToken)
    val paired = TeiReader.collectTokens(n, settings).head

    assert(paired.alternateReading.get.alternateCategory == Correction)
    assert(paired.alternateReading.get.readings.head.text == "better")
    assert(paired.readings.head.text == "oops")
  }
  it should "recognized paired orig/reg" in {
    val origReg = "<p><choice><orig>Achilles</orig><reg>Akhilleus</reg></choice> answer.</p>"
    val n = XML.loadString(origReg)
    val settings = TokenSettings(context, LexicalToken)
    val paired = TeiReader.collectTokens(n, settings).head

    assert(paired.alternateReading.get.alternateCategory == Multiform)
    assert(paired.alternateReading.get.readings.head.text == "Akhilleus")
    assert(paired.readings.head.text == "Achilles")
  }



}

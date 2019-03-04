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
    assert(alt.reading.head.text == "out")
    assert(alt.reading.head.status == Clear)

  }
  it should "recognize independent occurrences of TEI del" in pending


  it should "recognized paired abbr/expan" in pending
  it should "recognized paired sic/corr" in pending
  it should "recognized paired orig/reg" in pending



}

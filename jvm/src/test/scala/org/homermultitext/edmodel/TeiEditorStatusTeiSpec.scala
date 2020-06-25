package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiEditorStatusTeiSpec extends FlatSpec {


  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")

  // Status of HMT editor's reading
  "The TeiReader object" should  "recognize unclear readings" in {
    val n = XML.loadString("<unclear>α</unclear>")
    val settings = TokenSettings(context, HmtLexicalToken)

    val unclearTokens = TeiReader.collectTokens(n, settings)
    val expectedSize = 1
    val expectedCategory = HmtLexicalToken
    assert(unclearTokens.size == expectedSize)
    assert(unclearTokens(0).lexicalCategory == expectedCategory)
    val expectedReadings = 1
    val expectedText = "α"
    assert(unclearTokens(0).readings.size == expectedReadings)
    assert(unclearTokens(0).readings(0).text == expectedText)
  }

  it should "assign the correct editorial status to unclear readings" in {
      val n = XML.loadString("<unclear>α</unclear>")
      val settings = TokenSettings(context, HmtLexicalToken)

      val unclearToken = TeiReader.collectTokens(n, settings).head
      val reading = unclearToken.readings.head
      assert(reading.status == Unclear)
  }

  it should "flag XML elements inside unclear as an error" in {
    val badNesting = "<unclear><num value=\"1\">α</num></unclear>"
    val  n = XML.loadString(badNesting)
    val settings = TokenSettings(context, HmtLexicalToken)
    val unclearTokens = TeiReader.collectTokens(n, settings)
    val errors = unclearTokens.map(_.errors).flatten
    val expected = "Elements out of place in markup hierarchy. 'unclear' may not contain child element 'num'"
    assert(errors.size == 1)
    assert(errors.head == expected)
  }


  it should "recognize independent occurrences of TEI sic" in {
    val sic = "<p>The volcano is <sic>yowza</sic></p>"
    val  n = XML.loadString(sic)
    val settings = TokenSettings(context, HmtLexicalToken)
    val sicTokens = TeiReader.collectTokens(n, settings)
    val errors = sicTokens.map(_.errors).flatten
    assert(errors.isEmpty)
    val sicReading = (sicTokens.last.readings.head)
    assert(sicReading.status == Sic)
  }

  it should "flag XML elements inside sic as an error" in {
    val badNesting = "<sic><num value=\"1\">14</num></sic>"
    val  n = XML.loadString(badNesting)
    val settings = TokenSettings(context, HmtLexicalToken)
    val sicTokens = TeiReader.collectTokens(n, settings)
    val errors = sicTokens.map(_.errors).flatten
    val expected = "Elements out of place in markup hierarchy. 'sic' may not contain child element 'num'"
    assert(errors.size == 1)
    assert(errors.head == expected)
  }

  it should "recognize TEI gap" in {
    val gapped = "<p>The volcano is eru<gap/></p>"
    val  n = XML.loadString(gapped)
    val settings = TokenSettings(context, HmtLexicalToken)
    val gappedTokens = TeiReader.collectTokens(n, settings)
    val errors = gappedTokens.map(_.errors).flatten
    // A gap is a tokenizing error by definition:
    assert(errors.size == 1)
    val expected = "Lacuna in text: no tokens legible"
    assert(errors.head == expected)
  }


}

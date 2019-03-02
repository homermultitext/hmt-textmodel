package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiReaderTeiSpec extends FlatSpec {


  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")

  // Markup for document structure:
  "The TeiReader object" should "recognize structural use of TEI div for content of scholia" in pending
  it should "ignore structural use of TEI div for cross references of scholia" in pending
  it should "recognize structural use of TEI l" in pending
  it should "recognize structural use of TEI p" in pending
  it should "recognize structural use of TEI list" in pending
  it should "recognize structural use of TEI item" in pending
  it should "recognize structural use of TEI floatingText" in pending


  // Metadata to ignore
  it should "recognize and ignore TEI note elements in tokenization" in {
    val n = XML.loadString("<note>I really shouldn't be making these comments</note>")
    val settings = TokenSettings(context, LexicalToken)
    val noteTokens = TeiReader.collectTokens(n, settings)
    assert(noteTokens.isEmpty)
  }
  it should "recognize and ignore TEI ref elements in tokenization" in {
    val n = XML.loadString("<ref>URN value, normally</ref>")
    val settings = TokenSettings(context, LexicalToken)
    val refTokens = TeiReader.collectTokens(n, settings)
    assert(refTokens.isEmpty)
  }
  it should "recognize and ignore TEI figDesc elements in tokenization" in {
    val n = XML.loadString("<figDesc>Descriptions of beautiful figures are not part of the text</figDesc>")
    val settings = TokenSettings(context, LexicalToken)
    val figDescTokens = TeiReader.collectTokens(n, settings)
    assert(figDescTokens.isEmpty)
  }

  // Status of HMT editor's reading
  it should "recognize unclear readings" in {
    val n = XML.loadString("<unclear>α</unclear>")
    val settings = TokenSettings(context, LexicalToken)

    val unclearTokens = TeiReader.collectTokens(n, settings)
    val expectedSize = 1
    val expectedCategory = LexicalToken
    assert(unclearTokens.size == expectedSize)
    assert(unclearTokens(0).lexicalCategory == expectedCategory)
    val expectedReadings = 1
    val expectedText = "α"
    assert(unclearTokens(0).readings.size == expectedReadings)
    assert(unclearTokens(0).readings(0).text == expectedText)
  }
  it should "flag XML elements inside unclear as an error" in pending
  it should "recognize TEI gap" in pending

  it should "recognize independent occurrences of TEI sic" in pending
  it should "flag XML elements inside sic as an error" in pending



  // Status of scribe's reading:
  it should "recognize independent occurrences of TEI add" in pending
  it should "recognize independent occurrences of TEI del" in pending


  it should "recognized paired abbr/expan" in pending
  it should "recognized paired sic/corr" in pending
  it should "recognized paired orig/reg" in pending



  // Tokenizing markup
  it should "recognize TEI num element in tokenization" in {
    val n = XML.loadString("<num value=\"1\">α</num>")
    val settings = TokenSettings(context, LexicalToken)
    val numTokens = TeiReader.collectTokens(n, settings)
    val expectedSize = 1
    val expectedCategory = NumericToken
    assert(numTokens.size == expectedSize)
    assert(numTokens(0).lexicalCategory == expectedCategory)
    val expectedReadings = 1
    val expectedText = "α"
    assert(numTokens(0).readings.size == expectedReadings)
    assert(numTokens(0).readings(0).text == expectedText)
  }
  it should "recognize TEI foreign element in tokenization"
  it should "recognize TEI w element in tokenization"

  it should "nest unclear inside tokenizing elements" in {
    val n = XML.loadString("<num value=\"11\">ι<unclear>α</unclear></num>")
    val settings = TokenSettings(context, LexicalToken)

    val wrappedUnclearTokens = TeiReader.collectTokens(n, settings)

    val expectedSize = 1
    val expectedCategory = NumericToken
    assert(wrappedUnclearTokens.size == expectedSize)
    assert(wrappedUnclearTokens(0).lexicalCategory == expectedCategory)


    val expectedReadings = 2
    val expectedText0 = "ι"
    val expectedText1 = "α"
    assert(wrappedUnclearTokens(0).readings.size == expectedReadings)
    assert(wrappedUnclearTokens(0).readings(0).text == expectedText0)
    assert(wrappedUnclearTokens(0).readings(1).text == expectedText1)
  }

  // Semantic disambuation
  it should "recognize TEI persName element" in pending
  it should "recognize TEI placeName element" in pending
  it should "recognize TEI rs element" in pending
  it should "recognize TEI title element" in pending


  // Discourse analysis
  it should "recognize independent use of TEI q element" in pending
  it should "recognize use of q within cit element" in pending
  it should "recognize TEI cit element" in pending
  it should "recognize TEI ref element" in pending

}

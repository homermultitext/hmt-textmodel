package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiReaderTeiSpec extends FlatSpec {


  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")


  "The TeiReader object" should "correctly index gap elements" in {
    val gapped = "<p>The volcano is eru<gap/></p>"
    val c = Corpus(Vector(CitableNode(context, gapped)))
    val analyses = TeiReader.analyzeCorpus(c)

    val lacuna = analyses.filter(_.lexicalCategory == Lacuna).head
    val expectedError = "Lacuna in text: no tokens legible"
    assert(lacuna.errors.size == 1)
    assert(lacuna.errors.head == expectedError)

  }

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

  it should "nest unclear inside tokenizing elements" in  pending /*{
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
  }*/

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

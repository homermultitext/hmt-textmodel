package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiTokenizingSpec extends FlatSpec {


  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")


  // Tokenizing markup
  "The TeiReader object" should "do things" in pending


  it should "recognize TEI num element in tokenization" in {
    val n = XML.loadString("<num value=\"1\">α</num>")
    val settings = TokenSettings(context, LexicalToken)
    val numTokens = TeiReader.collectTokens(n, settings)
    assert(numTokens.size == 1)

    val tkn = numTokens.head
    val expectedCategory = NumericToken
    assert(tkn.lexicalCategory == expectedCategory)
    val expectedReadings = 1
    val expectedText = "α"
    assert(tkn.readings.size == expectedReadings)
    assert(tkn.readings.head.text == expectedText)
  }

  it should "recognize TEI foreign element in tokenization" in {
    val frn = "<foreign>voilà</foreign>"
    val n = XML.loadString(frn)
    val settings = TokenSettings(context, LexicalToken)
    val frnToken = TeiReader.collectTokens(n, settings).head
    val expectedCategory = LexicalToken
    assert(frnToken.lexicalCategory == expectedCategory)
    assert(frnToken.readings.head.text == "voilà")
  }
  it should "recognize TEI w element in tokenization" in {
    val wordWrap = "<p><w>partial<unclear>ly</unclear></w> unclear.</p>"
    val n = XML.loadString(wordWrap)
    val settings = TokenSettings(context, LexicalToken)
    val wrappedToken = TeiReader.collectTokens(n, settings).head
    println("\n\n" + wrappedToken)
  }
  it should "nest unclear inside tokenizing elements" in  {
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


}

package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiReaderObjectSpec extends FlatSpec {


  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")


  "The TeiReader object" should "collect white-space delimited tokens from a string" in {
    val  s = "Μῆνιν ἄειδε, θεά,"
    val settings = TokenSettings(context, LexicalToken)
    val tokenized =  TeiReader.tokensFromText(s, settings)
    val expectedSize = 5
    assert(tokenized.size == expectedSize)

    val firstToken = tokenized(0)
    val expectedFirstCat = LexicalToken
    assert (firstToken.lexicalCategory == expectedFirstCat)

    val third = tokenized(2)
    val expectedThirdCat = Punctuation
    assert(third.lexicalCategory == expectedThirdCat)
  }


  it should "encode colons in strings" in {
    val src = "voila:"
    val expected = "voila%3A"
    assert(TeiReader.ctsSafe(src) == expected)
  }

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


  it should "nest unclear inside tokenizing num element" in {
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

  it should "record an error of unclear contains another element" in {

  }

  it should "index tokens correctly from root elements" in {
    val n = XML.loadString("<TEI><text><body><div><l>Μῆνιν ἄειδε, <num value=\"11\">ι<unclear>α</unclear></num> θεά,</l></div></body></text></TEI>")
    val settings = TokenSettings(context, LexicalToken)

    val rootTokens = TeiReader.collectCitableTokens(n, settings)
    println("\n\n" + rootTokens.mkString("\n\n") + "\n\n")
  }


  it should "analyze a citable node" in {
     val txt = "<l>Μῆνιν ἄειδε, <num value=\"11\">ι<unclear>α</unclear></num> θεά,</l>"
     val cn = CitableNode(context, txt)
     val nodeTokens = TeiReader.analyzeCitableNode(cn)
     println("\n\n" + nodeTokens.mkString("\n\n") + "\n\n")
  }

    it should "analyze a citable corpus" in {
       val txt = "<l>Μῆνιν ἄειδε, <num value=\"11\">ι<unclear>α</unclear></num> θεά,</l>"
       val nodes = Vector(CitableNode(context, txt))
       val c = Corpus(nodes)
       val corpusTokens = TeiReader.analyzeCorpus(c)
       println("\n\n" + corpusTokens.mkString("\n\n") + "\n\n")
    }
}

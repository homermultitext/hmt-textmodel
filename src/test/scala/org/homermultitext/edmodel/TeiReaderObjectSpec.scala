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
  }




}

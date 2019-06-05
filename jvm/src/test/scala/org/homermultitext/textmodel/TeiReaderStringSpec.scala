package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiReaderStringSpec extends FlatSpec {


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

}

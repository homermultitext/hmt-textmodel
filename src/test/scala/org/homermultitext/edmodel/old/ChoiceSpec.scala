package org.homermultitext.edmodel
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._

class ChoiceSpec extends FlatSpec {


  val reader = TeiReaderOld("")
  val testUrn = CtsUrn("urn:cts:hmt:unittests.v1:1")

  "A TEI choice" should "contain a pair of elements" in pending

  it should "require pairing of abbr-expan" in {
    val missingExpan = "<p>Not a <choice><abbr>chc</abbr></choice></p>"
    val tokens = reader.teiToTokens(testUrn, missingExpan)
    assert (tokens.size == 3)
  }


  it should "require pairing of sic-corr" in pending
  it should "require pairing of orig-reg" in pending

  it should "enforce n attribute on non-empty ref" in pending
  it should "enforce non-empty q" in pending
  /*
<cit><ref></ref><q>quoted text</q></cit>
  */

  it should "require disambiguation wrapping ed. status" in pending
  it should "require disambiguation wrapping all forms of tokenziation" in pending


  it should "require NEs wrap abbr-expan" in pending

  it should "allow NEs within a orig-reg and sic-corr" in pending

  it should "require discourse analysis to wrap everything else" in pending

  /*

  */

  /*

  */




}

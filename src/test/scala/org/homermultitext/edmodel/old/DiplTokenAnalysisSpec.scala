package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import org.scalatest.FlatSpec



/** Test how TeiReader handles diplomatic
* readings in various situations.*/
class DiplTokenAnalysisSpec extends FlatSpec {


  val reader = TeiReader("")
  val testUrn = CtsUrn("urn:cts:hmt:unittests.v1:1")

  "A TokenAnalysis" should "skip added content when reading diplomatically" in {
    val addedText = "<p>Now is <add>the</add> time</p>"

    val tokens = reader.teiToTokens(testUrn, addedText)
    assert(tokens.size == 4)

    val diplCorpus = DiplomaticEditionFactory.corpusFromTokens(tokens)
    val expectedSize  = 3
    assert (diplCorpus.size == expectedSize)
  }

  it should "include deleted content when reading diplomatically" in {
    val deletedText = "<p>Now is the <del>the</del> time</p>"
    val tokens = reader.teiToTokens(testUrn, deletedText)
    assert(tokens.size == 5)

    val diplCorpus = DiplomaticEditionFactory.corpusFromTokens(tokens)
    assert(diplCorpus.size == 5)
  }

  it should "prefer sic in sic/corr choice" in {
    val choice = "<p>Please meet <choice><sic>Barack</sic><corr>Luther</corr></choice></p>"
    val tokens = reader.teiToTokens(testUrn, choice)
    assert(tokens.size == 3)

    val diplCorpus = DiplomaticEditionFactory.corpusFromTokens(tokens)
    assert(diplCorpus.nodes(2).text == "Barack")
  }


  it should "prefer orig in orig/reg choice" in pending

  it should "prefer del in add/del choice" in pending


}

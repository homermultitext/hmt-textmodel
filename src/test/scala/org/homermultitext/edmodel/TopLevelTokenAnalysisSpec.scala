package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import org.scalatest.FlatSpec

class TopLevelTokenAnalysisSpec extends FlatSpec {


  val reader = TeiReader("")


  "A TokenAnalysis" should "skip added content when reading diplomatically" in {
    val urn = CtsUrn("urn:cts:hmt:unittests.v1:1")
    val addedText = "<p>Now is <add>the</add> time</p>"

    val tokens = reader.teiToTokens(urn, addedText)
    assert(tokens.size == 4)

    val diplCorpus = DiplomaticEditionFactory.corpusFromTokens(tokens)
    val expectedSize  = 3
    assert (diplCorpus.size == expectedSize)
  }

  it should "include deleted content when reading diplomatically" in {
    val urn = CtsUrn("urn:cts:hmt:unittests.v1:1")
    val deletedText = "<p>Now is the <del>the</del> time</p>"

    val tokens = reader.teiToTokens(urn, deletedText)
    assert(tokens.size == 5)
    val diplCorpus = DiplomaticEditionFactory.corpusFromTokens(tokens)
    assert(diplCorpus.size == 5)
  }
}

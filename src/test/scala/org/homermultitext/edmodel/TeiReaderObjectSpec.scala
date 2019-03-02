package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiReaderObjectSpec extends FlatSpec {


  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")


  "The TeiReader object" should "index tokens correctly from root elements" in {
    val n = XML.loadString("<l>Μῆνιν ἄειδε, <num value=\"11\">ι<unclear>α</unclear></num> θεά,</l>")
    val settings = TokenSettings(context, LexicalToken)

    val rootTokens = TeiReader.collectCitableTokens(n, settings)

     val expectedCount = 6
     assert(rootTokens.size == expectedCount)
     val expectedLast = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA_lextokens:1.1.5@,[2]")
     assert(rootTokens.last.editionUrn == expectedLast)
  }

  it should "analyze a citable node" in {
     val txt = "<l>Μῆνιν ἄειδε, <num value=\"11\">ι<unclear>α</unclear></num> θεά,</l>"
     val cn = CitableNode(context, txt)
     val nodeTokens = TeiReader.analyzeCitableNode(cn)

     val expectedCount = 6
     assert(nodeTokens.size == expectedCount)
     val expectedLast = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA_lextokens:1.1.5@,[2]")
     assert(nodeTokens.last.editionUrn == expectedLast)
  }

  it should "analyze a citable corpus" in {
     val txt = "<l>Μῆνιν ἄειδε, <num value=\"11\">ι<unclear>α</unclear></num> θεά,</l>"
     val nodes = Vector(CitableNode(context, txt))
     val c = Corpus(nodes)
     val corpusTokens = TeiReader.analyzeCorpus(c)
      val expectedCount = 6
      assert(corpusTokens.size == expectedCount)
      val expectedLast = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA_lextokens:1.1.5@,[2]")
      assert(corpusTokens.last.editionUrn == expectedLast)
  }
}

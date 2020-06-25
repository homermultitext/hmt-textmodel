package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiReaderStructuralTeiSpec extends FlatSpec {


  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")

  // Markup for document structure:
  "The TeiReader object" should "recognize structural use of TEI div and p for content of scholia" in {
    val n = XML.loadString("<div><p>Long comment.</p></div>")
    val settings = TokenSettings(context, HmtLexicalToken)
    val divTokens = TeiReader.collectTokens(n, settings)
    val expectedSize = 3
    assert(divTokens.size == expectedSize)
    // no errors
    assert(divTokens.map(_.errors).flatten.isEmpty)

  }

  it should "recognize structural use of TEI  div and l for lines of the Iliad" in {
    val n = XML.loadString("<div><l>Sing, goddess.</l></div>")
    val settings = TokenSettings(context, HmtLexicalToken)
    val divTokens = TeiReader.collectTokens(n, settings)
    val expectedSize = 4
    assert(divTokens.size == expectedSize)
    // no errors
    assert(divTokens.map(_.errors).flatten.isEmpty)
  }


  it should "recognize structural use of TEI list and item" in {
    val n = XML.loadString("<list><item>Bullet one</item></list>")
    val settings = TokenSettings(context, HmtLexicalToken)
    val divTokens = TeiReader.collectTokens(n, settings)
    val expectedSize = 2
    assert(divTokens.size == expectedSize)
    // no errors
    assert(divTokens.map(_.errors).flatten.isEmpty)
  }

  it should "recognize the entire baroque TEI structure of floatingText" in {
    val scContext = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:8.123")

    val fig = XML.loadString("""<figure><figDesc>The scribe has drawn a spherical diagram delineating the aether, air, Hades, and Taratus</figDesc>
      <floatingText><body><p>αιθηρ</p></body></floatingText></figure>""")
    val settings = TokenSettings(scContext, HmtLexicalToken)
    val figTokens = TeiReader.collectTokens(fig, settings)
    println(figTokens)

  }

}

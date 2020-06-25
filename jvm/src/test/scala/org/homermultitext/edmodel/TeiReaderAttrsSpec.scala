package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiReaderAttrsSpec extends FlatSpec {


  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")

  // Markup for document structure:
  "The TeiReader object" should "accumluate error messages about attribute usage" in {
    val n = XML.loadString("<persName>Agamemnon</persName>")
    val settings = TokenSettings(context, HmtLexicalToken)
    val pnTokens = TeiReader.tokensFromElement(n, settings)
    assert (pnTokens.size == 1)
    assert(pnTokens(0).errors.nonEmpty)
    for ( (e, i) <- pnTokens(0).errors.zipWithIndex) {
      println(s"${i + 1}.  ${e}")
    }
    //println("PERSNAME TOKEN " + pnTokens(0))
    //println("With errors: " + pnTokens(0).errors.mkString("\n\n"))
  }

  it should "require a value attribute on num elements" in {
    val n = XML.loadString("<num>α</num>")
    val settings = TokenSettings(context, HmtLexicalToken)
    val numToken = TeiReader.tokensFromElement(n, settings).head
    assert(numToken.errors.nonEmpty)
    val expected = "For TEI 'num', @value attribute with numeric value of element's contents is mandatory."
    assert(numToken.errors.head == expected)
  }

  it should "require a valid integer on num elements' value attribute" in {
    val n = XML.loadString("<num value=\"alpha\">α</num>")
    val settings = TokenSettings(context, HmtLexicalToken)
    val numToken = TeiReader.tokensFromElement(n, settings).head
    assert(numToken.errors.size == 1)
    val expected = "For TEI 'num', @value attribute with numeric value of element's contents is mandatory."
    assert(numToken.errors.head == expected)
  }

}

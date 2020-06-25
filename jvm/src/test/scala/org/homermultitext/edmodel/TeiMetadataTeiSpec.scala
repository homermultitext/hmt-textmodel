package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiMetadataTeiSpec extends FlatSpec {


  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")

  // Metadata to ignore
  "The TeiReader object" should  "recognize and ignore TEI note elements in tokenization" in {
    val n = XML.loadString("<note>I really shouldn't be making these comments</note>")
    val settings = TokenSettings(context, HmtLexicalToken)
    val noteTokens = TeiReader.collectTokens(n, settings)
    assert(noteTokens.isEmpty)
  }

  it should "recognize and ignore TEI figDesc elements in tokenization" in {
    val n = XML.loadString("<figDesc>Descriptions of beautiful figures are not part of the text</figDesc>")
    val settings = TokenSettings(context, HmtLexicalToken)
    val figDescTokens = TeiReader.collectTokens(n, settings)
    assert(figDescTokens.isEmpty)
  }

}

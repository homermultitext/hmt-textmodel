package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiDisambiguationSpec extends FlatSpec {


  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")

  // Semantic disambuation
  "The TeiReader object" should "recognize a TEI persName element" in {
    val persElem = "<p><persName n=\"urn:cite2:hmt:pers.v1:pers1\">Achilles</persName></p>"
    val n = XML.loadString(persElem)
    val settings = TokenSettings(context, LexicalToken)

    val achilles = TeiReader.collectTokens(n, settings).head
    val expectedUrn = Cite2Urn("urn:cite2:hmt:pers.v1:pers1")

    assert(achilles.lexicalDisambiguation == expectedUrn)
  }

  it should "recognize TEI placeName element" in {
    val placeElem = "<p><placeName n=\"urn:cite2:hmt:place.v1:place1\">Athens</placeName></p>"
    val n = XML.loadString(placeElem)
    val settings = TokenSettings(context, LexicalToken)

    val athena = TeiReader.collectTokens(n, settings).head

    val expectedUrn = Cite2Urn("urn:cite2:hmt:place.v1:place1")
    assert(athena.lexicalDisambiguation == expectedUrn)

  }
  it should "recognize TEI title element" in {
    val titleElem = "<p><title n=\"urn:cite2:hmt:citedworks.v1:work1\">Odyssey</title></p>"
    val n = XML.loadString(titleElem)
    val settings = TokenSettings(context, LexicalToken)

    val athena = TeiReader.collectTokens(n, settings).head

    val expectedUrn = Cite2Urn("urn:cite2:hmt:citedworks.v1:work1")
    assert(athena.lexicalDisambiguation == expectedUrn)
  }
  it should "recognize TEI rs element" in pending




}

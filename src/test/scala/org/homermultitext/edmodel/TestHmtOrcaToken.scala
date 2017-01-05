package org.homermultitext.edmodel
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._

class OrcaTokenSpec extends FlatSpec {

  val tkn = HmtToken(
    analysis = CiteUrn("urn:cite:hmt:va_il_tkns.tkn1.v1"),
    editionUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA_tkns:1.1.1"),
    sourceUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1@μῆνιν"),

    lexicalCategory = LexicalToken,
    readings = Vector(Reading("μῆνιν",Clear))
  )

  "The HmtOrcaToken object" should "construct an HmtOrcaToken instance from an HmtToken and a key" in {
    val example1 = HmtOrcaToken(tkn,"dipl")
    example1 match {
      case hot: HmtOrcaToken => assert (1 == 1)
      case _ => fail("Object did not create right instance")
    }
  }

  it should "record the correct CtsUrn for the analyzed text" in {
      val example1 = HmtOrcaToken(tkn,"dipl")
      assert (example1.src == CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA_tkns:1.1.1"))
  }

  it should "construct the correct CtsUrn for the analytical exemplar" in {
    val example1 = HmtOrcaToken(tkn,"dipl")
    println("\n\n" + example1.orcaColumn)
  }



}

package org.homermultitext.edmodel
import org.scalatest.FlatSpec

class HmtTokenSpec extends FlatSpec {
  val tkn = HmtToken(urn = "urn:cts:greekLit:tlg0012.tlg001.msA.urtoken:1.1.1",
  lexicalCategory = LexicalToken,
  readings = Vector(EditedString("μῆνιν",Clear))
)
  "A token" should "have a vector of edited strings" in {
    assert (tkn.readings.size == 1)
    assert (tkn.readings(0).status == Clear)
    assert (tkn.readings(0).reading == "μῆνιν")
  }
  it should "have a lexical type" in {
    assert (tkn.lexicalCategory == LexicalToken)
  }
  it should "have a default lang value of 'grc'" in {
    assert (tkn.lang == "grc")
  }
  it should "have support explicitly set lang value" in pending
  it should "have a URN" in {
    assert (tkn.urn == "urn:cts:greekLit:tlg0012.tlg001.msA.urtoken:1.1.1")
  }
}

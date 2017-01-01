package org.homermultitext.edmodel
import org.scalatest.FlatSpec

class HmtTokenSpec extends FlatSpec {
  val tkn = HmtToken(urn = "urn:cts:greekLit:tlg0012.tlg001.msA.urtoken:1.1.1",
  lexicalCategory = LexicalToken,
  readings = Vector(Reading("μῆνιν",Clear)) )

  "A token" should "have a vector of readings" in {
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
  it should "have a URN" in {
    assert (tkn.urn == "urn:cts:greekLit:tlg0012.tlg001.msA.urtoken:1.1.1")
  }
  it should "support explicitly setting lang value" in pending

  it should "have a default of no alternate reading" in {
    assert(tkn.alternateReading.reading.size == 0)
    assert(tkn.alternateReading.alternateCategory == Original)
  }

  it should "have a default discourse category of DirectVoice" in {
    assert (tkn.discourse == DirectVoice)
  }
  it should "have a default for entity disambiguation of 'Automated disambiguation' DirectVoice" in {
    assert (tkn.lexicalDisambiguation == "Automated disambiguation")
  }

  "Pretty printing as a table row" should "create a string of 7 delimited items" in pending

   it should "default to using tab as a delimiting string" in pending

   it should "support specifiying a delimiting string" in pending

  "Pretty printing as a column" should  "create a string of 7 rows" in {
    val columns = tkn.columnString.split("\n")
    assert (columns.size == 7)
  }

  it should  "include labels by default" in {
    val columns = tkn.columnString.split("\n")


  }

  it should  "support turning off labels" in pending


}

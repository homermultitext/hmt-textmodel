package org.homermultitext.edmodel
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._

import scala.collection.mutable.ArrayBuffer

class HmtTokenSpec extends FlatSpec {
  val tkn = HmtToken(urn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.urtoken:1.1.1"),
  sourceUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1@μῆνιν"),
  analysis = CiteUrn("urn:cite:hmt:urtoken.1.v1"),

  lexicalCategory = LexicalToken,
  readings = Vector(Reading("μῆνιν",Clear))
  )


  "A token"  should "have a URN" in {
    assert (tkn.urn == CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA.urtoken:1.1.1"))
  }

  it should "be indexed to a CTS URN with subref" in {
    assert (tkn.sourceUrn == CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1@μῆνιν"))

  }
  it should "have an analysis URN" in {
    assert (tkn.analysis == CiteUrn("urn:cite:hmt:urtoken.1.v1"))
  }

  it should "have a vector of readings" in {
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

  it should "really have CiteUrn objects for entity disambiguation" in pending
  it should "have a default for entity disambiguation of 'Automated disambiguation'" in {
    assert (tkn.lexicalDisambiguation == CiteUrn("urn:cite:hmt:disambig.lexical.v1"))
  }

  it should "have a default discourse category of DirectVoice" in {
    assert (tkn.discourse == DirectVoice)
  }
  it should "have a default of no alternate reading" in {
    assert(tkn.alternateReading.reading.size == 0)
    assert(tkn.alternateReading.alternateCategory == Original)
  }



  "Pretty printing as a table row" should "create a string of 11 delimited items" in {
    // no errors: so splitting finds only 8 non-empty columns
    val columns = tkn.rowString.split("\t")
    assert (columns.size == 10)
    val errorToken = tkn.copy(errors = ArrayBuffer("dummy error message"))
    val errorColumns = errorToken.rowString.split("\t")
    assert (errorColumns.size == 11)
  }

   it should "default to using tab as a delimiting string" in {
     val columns = tkn.rowString.split("\t")
     assert (columns.size == 10)
   }

   it should "support specifiying a delimiting string" in {
     tkn.propertySeparator = "#"
     val columns = tkn.rowString.split("#")
     assert (columns.size == 10)
   }

  "Pretty printing as a column" should  "create a string of 11 rows" in {
    val columns = tkn.columnString.split("\n")
    assert (columns.size == 11)
  }

  it should  "include labels by default" in {
    val columns = tkn.columnString.split("\n").map(_.split(": ")).map( ar => ar(0)).map(_.trim).toVector
    assert(columns == HmtToken.labels)
  }

  it should  "support turning off labels" in {
    val columns = tkn.columnString(false).split("\n")
    assert (columns(0) == "urn:cts:greekLit:tlg0012.tlg001.msA.urtoken:1.1.1")
  }


}

package org.homermultitext.edmodel
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._

import scala.collection.mutable.ArrayBuffer

class HmtTokenSpec extends FlatSpec {
  val tkn = HmtToken(
    analysis = Cite2Urn("urn:cite2:hmt:va_il_tkns.v1:tkn1"),
    editionUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA_tkns:1.1.1"),
    sourceUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1@μῆνιν"),

    lexicalCategory = LexicalToken,
    readings = Vector(Reading("μῆνιν",Clear))
  )


  "A token analysis"  should "have an identifying URN" in {
    assert (tkn.analysis == Cite2Urn("urn:cite2:hmt:va_il_tkns.v1:tkn1") )
  }


  it should "be indexed to a CTS URN" in {
    assert (tkn.sourceUrn == CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1@μῆνιν"))
  }

  it should "have a CTS URN situating the token in an analytical edition" in {
    assert (tkn.editionUrn == CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA_tkns:1.1.1"))
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

  it should "have Cite2Urn objects for entity disambiguation" in {
    tkn.lexicalDisambiguation match {
      case u: Cite2Urn => assert (1 == 1)
      case _ => fail("Wrong class of lexical disambiguation for " + tkn)
    }
  }
  it should "have a default Cite2Urn for entity disambiguation with string value 'urn:cite2:hmt:disambig.v1:lexical'" in {
    assert (tkn.lexicalDisambiguation == Cite2Urn("urn:cite2:hmt:disambig.v1:lexical"))
  }

  it should "have a default discourse category of DirectVoice" in {
    assert (tkn.discourse == DirectVoice)
  }
  it should "have a default of no alternate reading" in {
    tkn.alternateReading match {
      case Some(alt) => {
        fail("Should not have found alternate reading found for " + tkn)
      }
      case None => assert ( 1 == 1)
    }
  }

  "Pretty printing as a table row" should "create a string of 11 delimited items" in {
    // no errors: so splitting finds only 10 non-empty columns
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
    assert (columns(0) == "urn:cite2:hmt:va_il_tkns.v1:tkn1")
  }

  "Formatting strings for various text deformations" should "include only clear and unclear readings in diplomatic" in {
    val tkn = HmtToken(
      analysis = Cite2Urn("urn:cite2:hmt:va_schA_tkns:tkn70"),
      editionUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt_tkns:1.7.comment.70"),
      sourceUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.7.comment@ἄνοι[1]"),

      lexicalCategory = LexicalToken,
      readings = Vector(Reading("ἄνοι",Clear)),
      alternateReading = Some(
        AlternateReading(Restoration,Vector(
          Reading( "ἄνθρωποι",Restored)))
      )
    )
    assert(tkn.leidenDiplomatic == "ἄνοι")
  }

  it should "distinguish unclear readings in diplomatic deformation" in {
    val tkn = HmtToken(
      analysis = Cite2Urn("urn:cite2:hmt:va_schA_tkns:tkn21"),
      editionUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt_tkns:19.hc_19.comment.21"),
      sourceUrn = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:19.hc_19.comment@αὐτῶ[0]"),

      lexicalCategory = LexicalToken,
      readings = Vector(Reading("αὐτ",Clear), Reading("ῶ",Unclear))
    )

    assert(tkn.leidenDiplomatic == "αὐτῶ?")
  }

  //			grc		LexicalToken	urn:cite2:hmt:disambig.v1:lexical	None	DirectVoice	None

}

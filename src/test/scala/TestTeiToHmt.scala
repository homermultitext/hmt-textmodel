package org.homermultitext.edmodel
import org.scalatest._

class TeiIngestionSpec extends FlatSpec with Inside {


  "The TeiReader object" should "convert well-formed HMT TEI to a Vector of (urn, HmtToken) tuples" in {
    val xml = """<div type="scholion" n="hc_5" xmlns="http://www.tei-c.org/ns/1.0"><div type="lemma"> <p/></div><div type="comment"> <p> <choice> <abbr> ουτ</abbr> <expan> οὕτως</expan></choice> δια τοῦ <rs type="waw"> ο</rs> <q> ζεύγνυον</q> ⁑</p></div></div>"""
    val urn = "u:"
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val firstEntry = tokenV(0)
    inside (firstEntry._2) {
      case HmtToken(u,lng,rdgs,lexcat,disambig,alt,disc) => {
        assert (lng == "grc")
      }
      case _ => fail("Object is not a HmtToken")
    }
  }

}

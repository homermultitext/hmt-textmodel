package org.homermultitext.edmodel
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._
import java.text.Normalizer
import scala.collection.mutable.ArrayBuffer

class HmtTokenReadingSpec extends FlatSpec {


  val xml = """<div type="scholion" n="hc_5" xmlns="http://www.tei-c.org/ns/1.0"><div type="lemma"> <p/></div><div type="comment"> <p> <choice> <abbr> ουτ</abbr> <expan> οὕτως</expan></choice> δια τοῦ <rs type="waw"> ο</rs> <q> ζεύγνυον</q> ⁑</p></div></div>"""
  val urn = CtsUrn("urn:cts:greekLit:tlg5026.msAint.hmt:19.hc_5")
  val analysisV = TeiReader.teiToTokens(urn, xml)


  "A token analysis"  should "have a URN identifyiing the text node" in  {
    for (a <- analysisV) {
      a.textNode match {
        case u: CtsUrn => assert(true)
        case _ => fail("Should have retrieved a CtsUrn")
      }
    }
  }
  it should "have an analysis object" in {
    for (a <- analysisV) {
      a.analysis match {
        case t: HmtToken => assert(true)
        case _ => fail("Should have retrieved a HmtToken")
      }
    }
  }

  it should "match diplomatic text of tokens" in {

    val testToken = analysisV(2)
    val formC =  Normalizer.normalize("τοῦ", Normalizer.Form.NFC)
    val formD =  Normalizer.normalize("τοῦ", Normalizer.Form.NFD)

    assert(testToken.analysis.diplomaticMatch(formC))
    assert(testToken.analysis.diplomaticMatch(formD))
}


}

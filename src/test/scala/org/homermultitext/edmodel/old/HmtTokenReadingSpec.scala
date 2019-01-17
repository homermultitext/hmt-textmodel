package org.homermultitext.edmodel
import org.scalatest.FlatSpec
import edu.holycross.shot.cite._
import java.text.Normalizer
import scala.collection.mutable.ArrayBuffer

class HmtTokenReadingSpec extends FlatSpec {


  val xml = """<div type="scholion" n="hc_5" xmlns="http://www.tei-c.org/ns/1.0"><div type="lemma"> <p/></div><div type="comment"> <p> <choice> <abbr> ουτ</abbr> <expan> οὕτως</expan></choice> δια τοῦ <rs type="waw"> ο</rs> <q> ζεύγνυον</q> ⁑</p></div></div>"""
  val urn = CtsUrn("urn:cts:greekLit:tlg5026.msAint.hmt_xml:19.hc_5")



  "A token analysis"  should "have a URN identifying the text node" in  {
    val reader = TeiReaderOld("")
    val analysisV = reader.teiToTokens(urn, xml)
    for (a <- analysisV) {
      a.textNode match {
        case u: CtsUrn => assert(true)
        case _ => fail("Should have retrieved a CtsUrn")
      }
    }
  }
  it should "have an analysis object" in  {
    val reader = TeiReaderOld("")
    val analysisV = reader.teiToTokens(urn, xml)
    for (a <- analysisV) {
      a.analysis match {
        case t: HmtToken => assert(true)
        case _ => fail("Should have retrieved a HmtToken")
      }
    }
  }

  it should "match diplomatic text of tokens" in {
    val reader = TeiReaderOld("")
    val analysisV = reader.teiToTokens(urn, xml)
    val testToken = analysisV(2)
    val formC =  Normalizer.normalize("τοῦ", Normalizer.Form.NFC)
    val formD =  Normalizer.normalize("τοῦ", Normalizer.Form.NFD)
    assert(formC != formD)
    assert(testToken.analysis.diplomaticMatch(HmtChars.hmtNormalize(formC)))
    assert(testToken.analysis.diplomaticMatch(HmtChars.hmtNormalize(formD)))
  }

  it should "match alternate text of tokens" in {
    val reader = TeiReaderOld("")
    val analysisV = reader.teiToTokens(urn, xml)
    val testToken = analysisV(0)
    val formC =  Normalizer.normalize("οὕτως", Normalizer.Form.NFC)
    val formD =  Normalizer.normalize("οὕτως", Normalizer.Form.NFD)
    assert(formC != formD)
    assert(testToken.analysis.alternateMatch(HmtChars.hmtNormalize(formC)))
    assert(testToken.analysis.alternateMatch(HmtChars.hmtNormalize(formD)))
  }

  it should "match diplomatic texts accent-free" in {
    val reader = TeiReaderOld("")
    val analysisV = reader.teiToTokens(urn, xml)
    val testToken = analysisV(2)
    val formC =  Normalizer.normalize("του", Normalizer.Form.NFC)
    val formD =  Normalizer.normalize("του", Normalizer.Form.NFD)
    assert(formC == formD)
    assert(testToken.analysis.diplomaticMatch(formC, false))
    assert(testToken.analysis.diplomaticMatch(formD, false))
  }


  it should "match alternate text of tokens accent-free" in pending/* {

    val testToken = analysisV(0)
    val formC =  Normalizer.normalize("ουτως", Normalizer.Form.NFC)
    val formD =  Normalizer.normalize("ουτως", Normalizer.Form.NFD)
    assert(formC == formD)
    assert(testToken.analysis.alternateMatch(formC, false))
    assert(testToken.analysis.alternateMatch(formD, false))
  }*/

}

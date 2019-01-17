package org.homermultitext.edmodel
import org.scalatest._
import scala.xml._

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

class TeiIngestScholionSpec extends FlatSpec {


  "The TeiReader object"  should "convert well-formed HMT TEI to a Vector of TokenAnalysis objects" in  {
    val xml = """<div type="scholion" n="hc_5" xmlns="http://www.tei-c.org/ns/1.0"><div n="lemma"> <p/></div><div n="comment"> <p> <choice> <abbr> ουτ</abbr> <expan> οὕτως</expan></choice> δια τοῦ <rs type="waw"> ο</rs> <q> ζεύγνυον</q> ⁑</p></div></div>"""
    val urn = CtsUrn("urn:cts:greekLit:tlg5026.msAint.hmt_xml:19.hc_5")
    val reader = TeiReader("")
    val analysisV = reader.teiToTokens(urn, xml)

    val tfs = for (analysis <- analysisV) yield {
      analysis match {
        case ta: TokenAnalysis => true
        case _ => false
      }
    }
    val bottomLine = tfs.distinct
    assert(bottomLine == Vector(true))
  }

}

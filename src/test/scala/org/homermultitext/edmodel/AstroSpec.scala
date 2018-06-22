package org.homermultitext.edmodel
import org.scalatest._
import scala.xml._

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

class AstroSpec extends FlatSpec {


  "The TeiReader object"  should "use n attribute of TEI rs element for lexical disambiguation type attribute is astro" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msA.hmt_xml:10.2501.comment")
    val xml = """<div n="comment"><p> ἄστρον ἐστιν ἀστέρων συμφόρημα ὡς
<rs type="astro"                                     n="urn:cite2:hmt:astro.v1:astro1">Ὠρίων</rs>
<rs type="astro" n="urn:cite2:hmt:astro.v1:.astro2">Ὁφιοῦχος</rs>.</p></div>
"""
    val tokenV = TeiReader.teiToTokens(urn, xml)

    val orion = Cite2Urn("urn:cite2:hmt:astro.v1:astro1")
    /*val trojans = tokenV(2).analysis
    assert(trojans.lexicalDisambiguation == Cite2Urn("urn:cite2:hmt_xml:place.v1:place6"))
    */
    val actual = tokenV(5).analysis.lexicalDisambiguation
    assert(actual == orion)
  }

}

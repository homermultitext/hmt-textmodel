package org.homermultitext.edmodel
import org.scalatest._
import scala.xml._

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

class AstroSpec extends FlatSpec {


  "The TeiReader object"  should "use n attribute of TEI rs element for lexical disambiguation type attribute is astro" in {

    val astroUrn = CtsUrn( "urn:cts:greekLit:tlg5026.msA.hmt_xml:10.2501.comment")
    val astroXml = """<div n="comment"><p> ἄστρον ἐστιν ἀστέρων συμφόρημα ὡς
<rs type="astro"                                     n="urn:cite2:hmt:astro.v1:astro1">Ὠρίων</rs>
<rs type="astro" n="urn:cite2:hmt:astro.v1:.astro2">Ὁφιοῦχος</rs>.</p></div>
"""
    val astroReader = TeiReader(astroXml)
    val astroTokens = astroReader.teiToTokens(astroUrn, astroXml)

    println("PARSED astroXml and got " + astroTokens.size + " tokens")
    val orion = Cite2Urn("urn:cite2:hmt:astro.v1:astro1")
    val actual = astroTokens(5).analysis.lexicalDisambiguation

    if (actual == orion){
      // ok
    } else {

      println("AstroSpec: FAILED.  Found " + astroTokens.size + " tokens")
      println("Sub index 5 = " + astroTokens(5).analysis.editionUrn.passageComponent + " " + astroTokens(5).analysis.readWithDiplomatic)

      for ((t,i) <- astroTokens.zipWithIndex) {
        println("\t" + i + " = " + t.analysis.readWithDiplomatic)
      }
      assert(actual == orion)
    }

  }

}

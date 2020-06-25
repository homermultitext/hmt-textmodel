package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiFloatRefSpec extends FlatSpec {

val floatingRef = """<p> τὸ
<q>πλήθει</q>
<ref>urn:cts:greekLit:tlg0012.tlg001.msA:23.639</ref>
</p>
"""
  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")


  // Discourse analysis
  "The TeiReader object" should "correctly handle floating <ref> elements" in {
    val n = XML.loadString(floatingRef)
    val settings = TokenSettings(context, HmtLexicalToken)
    val tkns = TeiReader.tokensFromElement(n, settings)
    println(tkns.map(_.readings).mkString("\n"))
  }

}

package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiCitSpec extends FlatSpec {


  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")


  // Discourse analysis
  "The TeiReader object" should "recognize use of q within cit element" in {
    val quoteElem = "<p><cit><ref type=\"urn\">urn:cts:greekLit:tlg0012.tlg001:1.1</ref><q>μῆνιν</q></cit></p>"
    val n = XML.loadString(quoteElem)
    val settings = TokenSettings(context, LexicalToken)
    val quoted = TeiReader.collectTokens(n, settings)
    println("QUOTED: " + quoted)


    //println("DIRECT FROM TeiReader: " + TeiReader.tokensFromElement(n, settings))
    /*
    val expectedCategory = QuotedLanguage
    assert(quoted.head.discourse == expectedCategory)
*/
  }



}

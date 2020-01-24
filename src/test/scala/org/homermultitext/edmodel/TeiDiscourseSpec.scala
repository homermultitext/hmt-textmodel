package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiDiscourseSpec extends FlatSpec {


  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")


  // Discourse analysis
  "The TeiReader object" should "recognize independent use of TEI q element" in {
    val quoteElem = "<p>He said, <q>Impossible!</q></p>"
    val n = XML.loadString(quoteElem)
    val settings = TokenSettings(context, LexicalToken)
    val quoted = TeiReader.collectTokens(n, settings).last
    val expectedCategory = QuotedLanguage
    assert(quoted.discourse == expectedCategory)
  }
  it should "recognize use of q within cit element" in  {
    val quoteElem = "<p><cit><ref type=\"urn\">urn:cts:greekLit:tlg0012.tlg001:1.1</ref><q>Sing, goddess</q></cit></p>"
    val n = XML.loadString(quoteElem)
    val settings = TokenSettings(context, LexicalToken)
    val quoted = TeiReader.collectTokens(n, settings).head

    val expectedCategory = QuotedText
    assert(quoted.discourse == expectedCategory)

  }

  it should "reject TEI ref element outside of cit" in {
    val quoteElem = "<p><ref type=\"urn\">urn:cts:greekLit:tlg0012.tlg001:1.1</ref><q>μῆνιν</q></p>"
    val n = XML.loadString(quoteElem)
    val settings = TokenSettings(context, LexicalToken)
    val misquoted = TeiReader.collectTokens(n, settings)
    //println(misquoted.mkString("\n\n"))

    assert(misquoted.last.readings.size == 1)
    assert(misquoted.last.readings.head.text == "μῆνιν")

  }


}

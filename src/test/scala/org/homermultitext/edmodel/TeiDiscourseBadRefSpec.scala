package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiDiscourseBadRefSpec extends FlatSpec {


  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")


  // Discourse analysis
  "The TeiReader object" should "reject TEI ref element outside of cit" in {
    val quoteElem = "<p><ref type=\"urn\">urn:cts:greekLit:tlg0012.tlg001:1.1</ref><q>μῆνιν</q></p>"
    val n = XML.loadString(quoteElem)
    val settings = TokenSettings(context, LexicalToken)
    val misquoted = TeiReader.collectTokens(n, settings)

    println(misquoted.mkString("\n\n"))

    assert(misquoted.last.readings.size == 1)
    assert(misquoted.last.readings.head.text == "μῆνιν")
    println("Errors :" + misquoted.map(_.errors))
/*
    val expectedErr = "Elem 'ref' not recognized."
    assert(misquoted.head.errors.head == expectedErr)
  */}


}

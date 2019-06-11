package org.homermultitext.edmodel

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import scala.xml._
import org.scalatest.FlatSpec


class HmtTokenReadingSpec extends FlatSpec {

  "An HmtToken" should "in reading with alternate prefer expanded form in abbr-expan pair" in {
    val n = XML.loadString("<choice><abbr>Mr.</abbr><expan>Mister</expan></choice>")
    val context = CtsUrn("urn:cts:dev:examples.eg.1:1")
    val settings = TokenSettings(context, LexicalToken)
    val tokens = TeiReader.collectTokens(n, settings)
    //for (t <- tokens) {
      //println(t.readWithAlternate)
    //}
  }

  it should "recognize tilde as token-splitting punctuation" in  {
    val il10_1 = """<l n="1">ὣς οἱ μὲν <rs type= "ethnic" n="urn:cite2:hmt:place.v1:place6">Τρῶες</rs> φυλακὰς ἔχον~ αὐτὰρ <rs type= "ethnic" n="urn:cite2:hmt:place.v1:place96">Ἀχαιοὺς</rs></l>"""
    val n = XML.loadString(il10_1)
    val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msB:10.1")
    val settings = TokenSettings(context, LexicalToken)
    val tokens = TeiReader.collectTokens(n, settings)
    println("TOKENS: \n" + tokens.mkString("\n"))
    println(tokens.size + " tokens")
  }
}

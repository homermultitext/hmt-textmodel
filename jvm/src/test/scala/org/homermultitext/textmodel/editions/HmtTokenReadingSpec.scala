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
}

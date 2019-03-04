package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiHierarchySpec extends FlatSpec {


  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
  // Markup for document structure:
  "The TeiReader object" should "allow multiple levels of HMT markup" in {
    val validNesting = "<div><p><choice><abbr>Mr</abbr><expan>Mister</expan></choice></p><p><persName><del><unclear>Agamemnon</unclear></del></persName></p></div>"
    val n = XML.loadString(validNesting)
    val settings = TokenSettings(context, LexicalToken)
    val multiDepths = TeiReader.tokensFromElement(n, settings)

    val misplaced = multiDepths.map(_.errors.filter( e => e.contains("hierarchy"))).flatten
    assert(misplaced.isEmpty)
  }

  it should "catch record errors in hierarchical placement" in {
    val invalidNesting = "<div><p><del><persName><unclear>Agamemnon</unclear></persName></del></p></div>"
    val n = XML.loadString(invalidNesting)
    val settings = TokenSettings(context, LexicalToken)
    val multiDepths = TeiReader.tokensFromElement(n, settings)

    val misplaced = multiDepths.map(_.errors.filter( e => e.contains("hierarchy"))).flatten
    assert(misplaced.nonEmpty)
  }

}

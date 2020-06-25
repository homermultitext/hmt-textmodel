package org.homermultitext.edmodel

import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

class ScholiaOrthographySpec extends FlatSpec {

  "The ScholiaOrthography object" should "have a label" in {
    val expected = "Orthography of Iliadic scholia"
    assert(ScholiaOrthography.orthography == expected)
  }

  it should "identify valid code points" in {
    val scholion = "νῆας τὸν τόπον τῶν νηῶν~  Ἑλλήσποντον δὲ, τὴν μέχρι Σιγείου θάλασσαν⁑"
    val ok = ScholiaOrthography.validString(scholion)
    val errorsHilited = ScholiaOrthography.hiliteBadCps(scholion)
    assert(ok)
    assert(errorsHilited == scholion)
  }

  it should "tokenize a citable node" in {
     val txt = "νῆας τὸν τόπον τῶν νηῶν~  Ἑλλήσποντον δὲ, τὴν μέχρι Σιγείου θάλασσαν⁑"
     val urn = CtsUrn("urn:cts:greekLit:tlg5026.msB.hmt:23.msB_303r_4")
     val cn = CitableNode(urn, txt)

     val tkns = ScholiaOrthography.tokenizeNode(cn)
     println(tkns.mkString("\n"))
  }



}

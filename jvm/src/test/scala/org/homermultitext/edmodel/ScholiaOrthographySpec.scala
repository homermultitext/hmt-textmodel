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

  it should "recognize tokens ending with Unicode for fishtail" in {
    val fishy = "θάλασσαν⁑"
    val clear = "θάλασσαν"
    assert(ScholiaOrthography.trailingFish(fishy))
    assert(ScholiaOrthography.trailingFish(clear) == false)
  }
  it should "account for trailing white space in checking for tailing fish" in {
    val padded = "θάλασσαν⁑ "
    assert(ScholiaOrthography.trailingFish(padded))
  }

  it should "strip trailing fish tail off a string" in {
    val padded = "θάλασσαν⁑ "
    val expected = "θάλασσαν"
    assert(ScholiaOrthography.stripFish(padded) == expected)
  }

  it should "split a string into a Vector of Strings" in {
    val comma  = "δὲ,"
    val depunctuated = (ScholiaOrthography.depunctuate(comma))
    assert(depunctuated.size == 2)
    assert(depunctuated(1) == ",")
  }

  it should "recognize fishtail in depunctuating"  in {
    val fishy = "θάλασσαν⁑ "
    val depunctuated = (ScholiaOrthography.depunctuate(fishy))
    assert(depunctuated.size == 2)
    assert(depunctuated(1) == "⁑")
  }

  it should "recognize leading and trailing quotes in tokenizing" in {
    val quoted = """ "Ἑλλήσποντον" """
    println(ScholiaOrthography.depunctuate(quoted))
  }

  it should "recognize trailing quote as punctuation" in {
    val trailingQuote = """νῆας" """
    val depunctuated = (ScholiaOrthography.depunctuate(trailingQuote))
    assert(depunctuated.size == 2)
  }


}

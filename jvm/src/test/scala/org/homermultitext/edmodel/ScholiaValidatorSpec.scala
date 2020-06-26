package org.homermultitext.edmodel

import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

class ScholiaValidatorSpec extends FlatSpec {

  "A ScholiaOrthoValidator" should "validate a citable node" in {
    val txt = "πτόλιν~ Κυπρίων τῶν ἐν Σαλαμῖνι ἡ λέξις~ κεῖται δὲ καὶ παρὰ κωμικῷ Ἀναξανδρίδῃ ἐν Σωσίππῳ⁑"
    val urn = CtsUrn("urn:cts:greekLit:tlg5026.burney86.hmt:ms_86_250r_1")
    val cn = CitableNode(urn, txt)
    val rslts = ScholiaOrthoValidator.validate(cn)
    val expectedResults = 18
    assert (rslts.size == expectedResults)
    val valid = rslts.filter(_.success)
    assert(valid.size == expectedResults)
  }
}

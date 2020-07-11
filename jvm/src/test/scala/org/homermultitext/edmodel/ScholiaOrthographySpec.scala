package org.homermultitext.edmodel

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import edu.holycross.shot.mid.orthography._

import org.scalatest.FlatSpec


class ScholiaOrthographySpec extends FlatSpec {

  "The ScholiaOrthography object" should "parse a citable node" in {
    val u = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:8.345.lemma")
    val s = "ὡς δ' ὅτ' ἐν οὐρανω ἄστρα φαεινὴν ἀμφὶ  ☾"
    val cn = CitableNode(u, s)
    val tokens = ScholiaOrthography.tokenizeNode(cn)
    val expectedSize = 9
    assert (tokens.size == expectedSize)

    assert(tokens.last.tokenCategory.get.name == "lexical")
    assert(tokens.last.string == "☾")
  }

}

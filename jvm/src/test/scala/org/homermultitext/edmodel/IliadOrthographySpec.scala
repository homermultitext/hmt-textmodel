package org.homermultitext.edmodel

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import edu.holycross.shot.mid.orthography._

import org.scalatest.FlatSpec


class IliadOrthographySpec extends FlatSpec {

  "The IliadOrthography object" should "do many things" in {
    val u = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.2")
    val s = "οὐλομένην~ ἡ μυρί' Ἀχαιοῖς ἄλγε' ἔθηκεν ~"
    val cn = CitableNode(u, s)
    val tokens = IliadOrthography.tokenizeNode(cn)
    val expectedSize = 8
    assert (tokens.size == expectedSize)

    assert(tokens.last.tokenCategory.get.name == "punctuation")
    assert(tokens.last.string == "~")
  }

}

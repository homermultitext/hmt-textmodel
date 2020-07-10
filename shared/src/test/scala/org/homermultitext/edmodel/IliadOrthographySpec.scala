package org.homermultitext.edmodel

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._

import org.scalatest.FlatSpec


class IliadOrthographySpec extends FlatSpec {

  "The IliadOrthography object" should "do many things" in {
    val u = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.2")
    val s = "οὐλομένην~ ἡ μυρί' Ἀχαιοῖς ἄλγε' ἔθηκεν ~"
    val cn = CitableNode(u, s)
    println(IliadOrthography.tokenizeNode(cn).size)
    println(IliadOrthography.tokenizeNode(cn).mkString("\n"))
  }

}

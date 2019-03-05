package org.homermultitext.edmodel

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._

import org.scalatest.FlatSpec


class DiplomaticReaderSpec extends FlatSpec {

  "The DiplomaticReader object" should "write diplomatic for a citable node" in {
    val txt = """<l n="1" xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0">Μῆνιν ἄειδε θεὰ <persName n="urn:cite2:hmt:pers.r1:pers1">Πηληϊάδεω Ἀχιλῆος</persName> </l>"""
    val urn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
    val cn = CitableNode(urn, txt)

    val diplNode = DiplomaticReader.editedNode(cn)
    println(diplNode)
  }

}

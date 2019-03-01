package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import org.scalatest.FlatSpec

class TeiReaderSpec extends FlatSpec {


  val il1_1 = CitableNode(
    CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA_xml:1.1"),
    s"""<l n="1">Μῆνιν ἄειδε θεὰ <persName n="urn:cite2:hmt:pers.r1:pers1">Πηληϊάδεω Ἀχιλῆος</persName>s</l>""")

  "A TeiReader" should "parse a node for a named entity edition" in {
    val reader = TeiReader(HmtNamedEntityEdition)
    println("NE edition " + reader.editedNode(il1_1))
  }
  it should "parse a node for a diplomatic edition" in pending
  it should "parse a node for a scribally normalized edition" in pending
  it should "parse a node for an editorially normalized edition" in pending
}

package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiReaderTeiSpec extends FlatSpec {


  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")


  "The TeiReader object" should "correctly index gap elements" in {
    val gapped = "<p>The volcano is eru<gap/></p>"
    val c = Corpus(Vector(CitableNode(context, gapped)))
    val analyses = TeiReader.analyzeCorpus(c)

    val lacuna = analyses.filter(_.lexicalCategory == HmtLacuna).head
    val expectedError = "Lacuna in text: no tokens legible"
    assert(lacuna.errors.size == 1)
    assert(lacuna.errors.head == expectedError)

  }






}

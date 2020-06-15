package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiTiersSpec extends FlatSpec {


  "The HmtTeiElements object" should "compute an optional index of tier level for an element name" in {
    val expectedMax = 4
    val expectedMin = 0
    assert(HmtTeiElements.tierDepth("q").get == expectedMax)
    assert(HmtTeiElements.tierDepth("unclear").get == expectedMin)
  }

  it should "ignore structural elements in computing markup tier" in {
    assert(HmtTeiElements.tierDepth("p") == None)
  }
}

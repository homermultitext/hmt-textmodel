package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import org.scalatest.FlatSpec


/** Test how TeiReader handles diplomatic
* readings in various situations.*/
class AltTokenAnalysisSpec extends FlatSpec {


  val reader = TeiReader("")
  val testUrn = CtsUrn("urn:cts:hmt:unittests.v1:1")

  "A TokenAnalysis" should "skip include added content when reading alternatively" in pending

  it should "skip deleted content when reading alternatively" in pending

  it should "prefer corr in sic/corr choice" in pending

  it should "prefer reg in orig/reg choice" in pending

  it should "prefer add in add/del choice" in pending


}

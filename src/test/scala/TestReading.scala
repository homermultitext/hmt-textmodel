package org.homermultitext.edmodel
import org.scalatest.FlatSpec

class ReadingSpec extends FlatSpec {

  val rdg: Reading = Reading("Now is the time", Clear)
  "A reading" should "make EditorialStatus available" in {
    assert(rdg.status == Clear)
  }

  it should "make text reading available" in {
    assert(rdg.reading == "Now is the time")
  }

  "Object's pretty printing function" should "show both reading and status" in {
    assert(Reading.typedText(rdg) == "Now is the time (clear)")
  }
}

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

  it should "use square bracket for restorations in leiden convention stringification" in {
    val restoration = Reading("not", Restored)
    assert(restoration.leidenize == "[not]")
  }

  it should "use question marks for unclear characters in leiden convention stringification" in {
    val restoration = Reading("not", Unclear)
    assert(restoration.leidenize == "n?o?t?")
  }

  it should "use the unaltered reading value for clear characters in leiden convention stringification" in {
    val restoration = Reading("not", Clear)
    assert(restoration.leidenize == "not")
  }


  it should "provide an object method creating a single Leiden string for a vector of readings" in pending

  "The Reading object's pretty printing function" should "show both reading and status" in {
    assert(rdg.typedText == "Now is the time (clear)")
  }
}

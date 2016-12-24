package org.homermultitext.edmodel
import org.scalatest.FlatSpec

class EditedStringSpec extends FlatSpec {

  val es: EditedString = EditedString("Now is the time", Clear)
  "constructor" should "make EditorialStatus available" in {
    assert(es.status == Clear)
  }

  "constructor" should "make text reading available" in {
    assert(es.reading == "Now is the time")
  }

  "Object's pretty printing function" should "show both reading and status" in {
    assert(EditedString.typedText(es) == "Now is the time (clear)")
  }
}

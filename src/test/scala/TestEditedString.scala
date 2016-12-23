package org.homermultitext.edmodel

import org.scalatest.FunSuite

class TestEditedString extends FunSuite {

  val es1: EditedString = EditedString("Now is the time", Clear)
  test("an edited string has a status") {
    assert(es1.status == Clear)
  }
}

package org.homermultitext.edmodel

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._

import org.scalatest.FlatSpec


class IliadStringSpec extends FlatSpec {

  "An IliadString" should "accept tilde as encoding of high stop in Unicode strings" in {
    val highStop = IliadString("οὐλομένην~")

    assert(highStop.ascii == "ou)lome/nhn~")
  }

  it should "accept tilde as encoding of high stop in ascii strings" in {
    val highStop = IliadString("ou)lome/nhn~")
    assert(highStop.ucode  == "οὐλομένην~")
  }

  it should "accept fishtail punctuation marker in unicode" in {
    val fishy = IliadString("ἑαυτοῖς ⁑")
    assert(fishy.ascii == "e(autoi=s ⁑")
  }

  it should "accept fishtail punctuation marker in ascii representation" in {
    val fishy = IliadString("e(autoi=s ⁑")
    assert(fishy.ucode == "ἑαυτοῖς ⁑")
  }

  it should "accept quantity markers on vowels" in {
    val longIota = IliadString("i_")
    val expectedLong = "ῑ"
    assert(longIota.ucode == expectedLong)

    val shortIota = IliadString("i^")
    val expectedShort = "ῐ"
    assert(shortIota.ucode == expectedShort)
  }
}

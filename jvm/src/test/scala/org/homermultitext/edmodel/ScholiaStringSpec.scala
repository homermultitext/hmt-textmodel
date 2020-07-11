package org.homermultitext.edmodel

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._

import org.scalatest.FlatSpec


class ScholiaStringSpec extends FlatSpec {

  "A ScholiaString" should "accept tilde as encoding of high stop in Unicode strings" in {
    val highStop = ScholiaString("οὐλομένην~")

    assert(highStop.ascii == "ou)lome/nhn~")
  }

  it should "accept tilde as encoding of high stop in ascii strings" in {
    val highStop = ScholiaString("ou)lome/nhn~")
    assert(highStop.ucode  == "οὐλομένην~")
  }

  it should "accept fishtail punctuation marker in unicode" in {
    val fishy = ScholiaString("ἑαυτοῖς ⁑")
    assert(fishy.ascii == "e(autoi=s ⁑")
  }

  it should "accept fishtail punctuation marker in ascii representation" in {
    val fishy = ScholiaString("e(autoi=s ⁑")
    assert(fishy.ucode == "ἑαυτοῖς ⁑")
  }

  it should "handle macra" in {
    val long = ScholiaString("a_")
    val expected = "ᾱ"
    assert(long.ucode == expected)
  }

  it should "handle  brevia" in {
    val short = ScholiaString("a^")
    val expected = "ᾰ"
    assert(short.ucode == expected)
  }
}

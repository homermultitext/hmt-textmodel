package org.homermultitext.edmodel
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import java.text.Normalizer

class CharsSpec extends FlatSpec {

  "The HmtChars object" should "determine if a character is legal" in {
    assert(HmtChars.legal('α'))
    assert(HmtChars.legal('a') == false)
  }

  it should "determine if a codepoint is legal" in {
    assert(HmtChars.legal('α'.toInt))
    assert(HmtChars.legal('a'.toInt) == false)
  }

  it should "determine if a character is a floating character "  in {
    assert(HmtChars.isFloating('+'))
    assert(HmtChars.isFloating('α') == false)
  }
  it should "determine if a codepoint is a floating character "  in {
    assert(HmtChars.isFloating('+'.toInt))
    assert(HmtChars.isFloating('α'.toInt) == false)
  }

  it should "determine if a character is a quantity marker "  in {
    assert(HmtChars.isQuantity('_'))
    assert(HmtChars.isQuantity('α') == false)
  }

  it should "determine if a codepoint is a quantity marker "  in {
    assert(HmtChars.isQuantity('_'.toInt))
    assert(HmtChars.isQuantity('α'.toInt) == false)
  }

  it should "determine if a character is a punctuation marker "  in {
    assert(HmtChars.isPunctuation(HmtChars.fishtail))
    assert(HmtChars.isPunctuation('α') == false)
  }
  it should "determine if a codepoint is a punctuation marker "  in {
    assert(HmtChars.isPunctuation(HmtChars.fishtail.toInt))
    assert(HmtChars.isPunctuation('α'.toInt) == false)
  }
  it should "normalize allowed input codepoints to specified output codepoints" in {
    val alphaTonos = Vector('\u03ac'.toInt)
    val alphaAcute = Vector('\u1f71'.toInt)
    assert (HmtChars.normalizeCPs(alphaTonos) == alphaAcute)
  }
  it should "handle bewitched upsilon+tonos" in {
    val deviltry = Vector('ύ'.toInt)



  }

  it should "find a vector of code points for a string" in {
    val cps = HmtChars.stringToCps("Homer")
    assert(cps.size == 5)
    assert(cps.isInstanceOf[Vector[Int]])
  }

  it should "compose a string from an Array of codepoints" in {
    val cps = HmtChars.stringToCps("Homer")
    assert (HmtChars.cpsToString(cps) == "Homer")
  }

  it should "normalize strings to HMT output form" in {
    val s1 = "ἐλθέ"
    val cps1 = HmtChars.stringToCps(s1)
    assert(cps1 ==  Vector(7952, 955, 952, 8051))

  }
}

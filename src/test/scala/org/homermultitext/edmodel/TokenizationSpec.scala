package org.homermultitext.edmodel
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._


class  TokenizationSpec extends FlatSpec {

  "A TeiReader" should "tokenize properly" in {
    val okInput = "urn:cts:greekLit:tlg002.tlg001.msA:10.2#<l n=\"2\">εὗδον παννύχιοι. μαλακῷ δεδμημένοι ὕπνῳ-</l>"
    val reader = TeiReader(okInput)
    val dipls = reader.tokens.map(_.analysis.readWithDiplomatic)

    println("ORIG:")
    for (t <-  dipls) {
      val good = HmtChars.legalChars(t)
      println(t + " ? " + good)
      if (! good) {
        val cps = HmtChars.badCPs(t)
        print("\tbad:  " + cps.map(_.toHexString) + " == " + HmtChars.cpsToString(cps))
      }
    }
    println("SANITIZED:")
    val sanitized = HmtChars.hmtNormalize(okInput)
    val reader2 = TeiReader(sanitized)
    val dipls2 = reader2.tokens.map(_.analysis.readWithDiplomatic)
    for (t <-  dipls2) {
      val good = HmtChars.legalChars(t)
      println(t + " ? " + good)
      if (! good) {
        val cps = HmtChars.badCPs(t)
        print("\tbad:  " + cps.map(_.toHexString) + " == " + HmtChars.cpsToString(cps))
      }
    }

  }

}

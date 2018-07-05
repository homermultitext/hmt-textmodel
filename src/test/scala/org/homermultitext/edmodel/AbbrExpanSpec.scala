package org.homermultitext.edmodel
import org.scalatest.FlatSpec


class AbbrExpanSpec extends FlatSpec {


  "An abbr-expan pair" should "santizie both readings" in {
    val theon = "θεόν"
    val srcCps = Vector(952, 949, 972, 957)
    assert(HmtChars.stringToCps(theon) == srcCps)
    val example = s"urn:cts:greekLit:tlg5026.msA.hmt:10.demo1#<p> <choice><abbr>θν</abbr><expan>${theon}</expan></choice>    ἱλάσκοντο</p>\n"

    val abbrexpan = TeiReader(example)
    val abbrexpanTokens = abbrexpan.tokens
    val theonAnalysis = abbrexpanTokens(0).analysis

    val alt = theonAnalysis.readWithAlternate
    val altCps = HmtChars.stringToCps(alt)
    assert (altCps != srcCps)

    val expected = Vector(952, 949, 8057, 957)


    assert(altCps == expected)
  }

}

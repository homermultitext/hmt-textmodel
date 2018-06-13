package org.homermultitext.edmodel
import org.scalatest.FlatSpec


class DeletionSpec extends FlatSpec {

  val example = """urn:cts:greekLit:tlg0012.tlg001.demo:24.212#<l  n="212" >ανδρι <add>παρα</add> κρατὲρῳ τοῦ εγω <del>δε</del> <w part="N">μ<unclear instant="false">ε</unclear><gap extent="2" unit="letters"/>ον</w> ἧπαρ εχοιμι</l>"""

  "A deletion" should "keep the deleted text as a reading" in {
    val analyses = TeiReader.fromString(example).map(_.analysis)
    println("Tokenized into " + analyses.size + " analyses.")
    for (a <- analyses) {
      val rdgs =  a.readings.mkString("++")
      println(rdgs + " ALT " + a.alternateReading)
    }

  }

  it should "have an empty string as thge atlernate" in pending
}

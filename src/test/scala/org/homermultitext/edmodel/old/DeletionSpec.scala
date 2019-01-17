package org.homermultitext.edmodel
import org.scalatest.FlatSpec


class DeletionSpec extends FlatSpec {


  "A deletion" should "keep the deleted text as a reading" in {
    val example = """urn:cts:greekLit:tlg0012.tlg001.demo:24.212#<l  n="212" >ανδρι <add>παρα</add> κρατὲρῳ τοῦ εγω <del>δε</del> <w part="N">μ<unclear instant="false">ε</unclear><gap extent="2" unit="letters"/>ον</w> ἧπαρ εχοιμι</l>"""

    val delReader = TeiReaderOld(example)
    val delTokens = delReader.tokens
    /*
    val analyses = TeiReaderOld.fromString(example).map(_.analysis)
    println("DeletionSpec: tokenized into " + analyses.size + " analyses.")
    for (a <- analyses) {
      val rdgs =  a.readings.mkString("++")
      //println(rdgs + " ALT " + a.alternateReading)
    }

*/
    println("PARSED deletion example and got " + delTokens.size + " tokens")
    for ((t,i) <- delTokens.zipWithIndex) {
      println("\t" + i + " = " + t.analysis.readWithDiplomatic)
    }


  }

  it should "have an empty string as the atlernate" in pending

  it should "create a valid CitableNode for an alternate reading" in pending /*{
      val xml = "urn:cts:greekLit:tlg0012.tlg001.demo:10.534#<l n=\"534\">ψεύσομαι. ἢ έτυμόν <del>τοι</del> ἐρέω, κέλεται δέ με θυμός·</l>"
      TeiReaderOld.clear
      val tokens = TeiReaderOld.fromString(xml)
      val analyses = tokens.map(_.analysis)
      for (tkn <- analyses) {
        //println(s"${tkn.editionUrn}==${tkn.readWithAlternate}")
      }
      for (tkn <- tokens) {
        //println(s"${tkn.analysis.editionUrn}==${tkn.analysis.readWithAlternate}")
      }
  }*/
}

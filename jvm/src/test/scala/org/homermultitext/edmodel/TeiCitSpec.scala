package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class TeiCitSpec extends FlatSpec {


val scholionXml = """
       <div n="comment">
          <p> <cit>
                <q><w>χόλ<unclear>ο</unclear>ν</w></q>
                <ref type="urn">urn:cts:greekLit:tlg0012.tlg001:1.192</ref>
             </cit>

             </p>
       </div>

"""


val scholionXml1 = """ <div ana="1a" n="1" type="scholion">
       <div n="comment">
          <p>
           οὕτω πρῶτον <rs type="waw"
                >ὀργή</rs> <rs type="waw">θυμός</rs> <rs type="waw">χόλος</rs>
                 ὅμως ὁ ποιητὴς ὡς
             συνωνύμοις ὀνόμασιν ἐπὶ <persName n="urn:cite2:hmt:pers.r1:pers1">Ἀχιλλεως</persName> χρῆται
             <cit>
                <q>ἢἐ <w>χόλ<unclear>ο</unclear>ν</w> παύσειεν</q>
                <ref type="urn">urn:cts:greekLit:tlg0012.tlg001:1.192</ref>
             </cit>

             </p>
       </div>
    </div>
"""

val floatingRef = """<p>ζητεῖται γὰρ τί εστι τὸ
<q>πλήθει πρόσθε βαλόντες</q>
<ref>urn:cts:greekLit:tlg0012.tlg001.msA:23.639</ref>
</p>
"""
  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")


  // Discourse analysis
  "The TeiReader object" should "recognize use of q within cit element" in  {
    val quoteElem = "<p><cit><ref type=\"urn\">urn:cts:greekLit:tlg0012.tlg001:1.1</ref><q>μῆνιν</q></cit></p>"
    val n = XML.loadString(quoteElem)
    val settings = TokenSettings(context, HmtLexicalToken)
    val quoted = TeiReader.collectTokens(n, settings)

    val expectedCategory = QuotedText
    assert(quoted.head.discourse == expectedCategory)
  }

  it should "parse valid HMT data" in {
    val n = XML.loadString(scholionXml1)
    val settings = TokenSettings(context, HmtLexicalToken)
    val tkns = TeiReader.tokensFromElement(n, settings)
    println("TOKENS:\n" + tkns.map(_.readings).mkString("\n"))
  }

  it should "correctly handle floating <ref> elements" in {
    val n = XML.loadString(floatingRef)
    val settings = TokenSettings(context, HmtLexicalToken)
    val tkns = TeiReader.tokensFromElement(n, settings)
  }

}

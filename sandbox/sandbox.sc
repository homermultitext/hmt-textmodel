import org.homermultitext.edmodel._

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.greek._
import scala.xml._


// Primitives: a CTS URN, associated with some XML texty
val urn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")
val  txt = "<TEI><text><body><div><l>Μῆνιν ἄειδε, <num value=\"11\">ι<unclear>α</unclear></num> θεά,</l></div></body></text></TEI>"


// How to make a citablenode for an OHCO2 edition
// from URN + XML primitives:
def diplEd(u: CtsUrn, xml: String): CitableNode = {
  val xmlNode  = CitableNode(u, xml)
  val diplNode = DiplomaticReader.editedNode(xmlNode)
  println("FROM XML SOURCE TEXT:\n" + xmlNode.text)
  println("\nGOT DIPLOMATIC TEXT:\n" + diplNode.text)
  diplNode
}

// How to make an alphabetically sorted list of unique
// words (lexical token) in a diplomatic reading of this text:
def wordList(urn: CtsUrn, xml: String) : Vector[String]= {
  val n = XML.loadString(xml)
  val settings = TokenSettings(urn, LexicalToken)
  // collect HMT tokens, limiting to lexical items:
  val lex = TeiReader.collectTokens(n, settings).filter(_.lexicalCategory == LexicalToken)
  // use Greek string library to lower case and sort,
  // mapping these ulitmately to unicode strings:
  val vocabList = lex.map(_.readWithDiplomatic).map(LiteraryGreekString(_).toLower).distinct.sortWith(_ < _).map(_.ucode)
  println("HERE'S YOUR RESULT:\n" + vocabList.mkString("\n"))
  vocabList
}

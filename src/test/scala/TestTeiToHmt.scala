package org.homermultitext.edmodel
import org.scalatest._

class TeiIngestionSpec extends FlatSpec with Inside {


  "The TeiReader object" should "convert well-formed HMT TEI to a Vector of (urn, HmtToken) tuples" in {
    val xml = """<div type="scholion" n="hc_5" xmlns="http://www.tei-c.org/ns/1.0"><div type="lemma"> <p/></div><div type="comment"> <p> <choice> <abbr> ουτ</abbr> <expan> οὕτως</expan></choice> δια τοῦ <rs type="waw"> ο</rs> <q> ζεύγνυον</q> ⁑</p></div></div>"""
    val urn = "urn:cts:greekLit:tlg5026.msAint.hmt:19.hc_5"
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val firstEntry = tokenV(0)
    println("first entry is " + firstEntry)
    inside (firstEntry._2) {

      case HmtToken(u,lng,rdgs,lexcat,disambig,alt,disc) => {
        println("Inside first, urn is " + u)
        assert (lng == "grc")
        assert (u == "urn:cts:greekLit:tlg5026.msAint.hmt:19.hc_5.1")
      }
      case _ => fail("Object is not a HmtToken")
    }
  }


  // tokenization
  it should "tokenize on white space by default" in pending

  it should "ignore white space within TEI w elements" in pending

  it should "treat individual punctuation characters as tokens" in pending

  // editorial status
  it should "default to editorial status of clear" in pending

  it should "record editorial status for readings of TEI unclear element as unclear" in pending

  it should "record editorial status for readings of TEI expan element as restored" in pending

  it should "record editorial status for readings of TEI add element as restored" in pending

  it should "record editorial status for readings of TEI reg element as restored" in pending


  // lexical category
  it should "default to lexical category of lexical item" in pending

  it should "categorize TEI num content as numeric lexical category" in pending

  it should "categorize punctuation characters as punctuation lexical category" in pending

  it should "categorize TEI rs content when @type = 'waw' as string literal lexical category" in pending

  it should "categorize TEI sic content as unparseable lexical category" in pending


  // language
  it should "default to value of grc for language" in pending

  it should "use n attribute for language on TEI foreign element" in pending


  // alternate reading
  it should "have one alternate reading object" in pending

  it should "have no alternate reading strings when alternate reading category is Original" in pending

  it should "have 1 or more alternate reading strings when alternate reading category is not Original" in pending

  it should "categorize alternate category of TEI expan as restoration" in pending

  it should "categorize alternate category of TEI add as multiform" in pending

  it should "categorize alternate category of TEI reg as multiform" in pending

  it should "categorize alternate category of TEI corr as correction" in pending

  // discourse category
  it should "default to value of direct voice for discourse category" in pending

  it should "categorize discourse of TEI cit as cited text" in pending

  it should "categorize discourse of TEI q outside of cit as quoted language" in pending

  // lexical disambigutation
  it should "default to value of automated morphological parsing for lexical disambiguation" in pending

  it should "categorize lexical disambiguation of TEI num as automated numerical parsing" in pending

  it should "use n attribute of TEI persName element for lexical disambiguation" in pending

  it should "use  n attribute of TEI placeName element for lexical disambiguation" in pending

  it should "use n attribute of TEI rs element for lexical disambiguation type attribute is ethnic" in pending
}

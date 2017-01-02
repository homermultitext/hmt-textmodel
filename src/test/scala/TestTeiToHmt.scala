package org.homermultitext.edmodel
import org.scalatest._

class TeiIngestionSpec extends FlatSpec with Inside {

  // test structure of result
  "The TeiReader object" should "convert well-formed HMT TEI to a Vector of (urn, HmtToken) tuples" in {

    val xml = """<div type="scholion" n="hc_5" xmlns="http://www.tei-c.org/ns/1.0"><div type="lemma"> <p/></div><div type="comment"> <p> <choice> <abbr> ουτ</abbr> <expan> οὕτως</expan></choice> δια τοῦ <rs type="waw"> ο</rs> <q> ζεύγνυον</q> ⁑</p></div></div>"""
    val urn = "urn:cts:greekLit:tlg5026.msAint.hmt:19.hc_5"

    val tokenV = TeiReader.teiToTokens(urn, xml)
    val firstEntry = tokenV(0)
    inside (firstEntry._2) {
      case HmtToken(u,lng,rdgs,lexcat,disambig,alt,disc,xsrc,errs) => {
        assert (lng == "grc")
        assert (u == "urn:cts:greekLit:tlg5026.msAint.hmt:19.hc_5.1")
      }
      case _ => fail("Object is not a HmtToken")
    }
  }

  // test tokenization
  it should "tokenize on white space by default" in {
    val urn = "urn:cts:greekLit:tlg5026.msAil.hmt:1.1303.comment"
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> ἢ απαρνησαι</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    assert(tokenV.size == 2)
  }

  it should "ignore white space within TEI w elements" in {
    val urn = "urn:cts:greekLit:tlg5026.msAext.hmt:15.8.comment"
    val xml =  """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> <w> <unclear> ἔν</unclear> θ'</w> εἴη</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    assert(tokenV.size == 2)
    assert (tokenV(0)._2.readings.size == 2)
  }

  it should "treat individual punctuation characters as tokens of lexical category punctuation" in {
    val urn = "urn:cts:greekLit:tlg5026.msAil.hmt:12.F20.comment"
    val xml  = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> βαρεῖ, </p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)

    val punctToken = tokenV(1)._2
    assert (punctToken.lexicalCategory == Punctuation)
  }

  it should "note instances of invalid element names" in {
    val urn = "urn:cts:greekLit:tlg5026.msA.hmt:1.39.comment"
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> ὁ θεὸς ὑπέσχετο τὸ <seg type="word"> κακ <choice> <sic> ὸν</sic> <corr> ῶς</corr></choice></seg> ἀπαλλάξειν</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)

    val correct = tokenV(0)._2
    val incorrect = tokenV(4)._2
    assert(correct.errors.size == 0)
    assert(incorrect.errors.size == 1)
  }



  // test all default values:
  it should "default to editorial status of clear" in {
    val urn = "urn:cts:greekLit:tlg5026.msAil.hmt:12.F20.comment"
    val xml  = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> βαρεῖ, </p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val token1readings = tokenV(0)._2.readings
    assert(token1readings(0).status == Clear)
   }
   it should "default to value of grc for language" in {
     val urn = "urn:cts:greekLit:tlg5026.msAil.hmt:12.F20.comment"
       val xml  = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> βαρεῖ, </p></div>"""
     val tokenV = TeiReader.teiToTokens(urn, xml)
     val token1 = tokenV(0)._2
     assert(token1.lang == "grc")
   }
   it should "default to lexical category of lexical item" in {
     val urn = "urn:cts:greekLit:tlg5026.msAil.hmt:12.F20.comment"
       val xml  = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> βαρεῖ, </p></div>"""
     val tokenV = TeiReader.teiToTokens(urn, xml)
     val token1 = tokenV(0)._2
     assert(token1.lexicalCategory == LexicalToken)
   }
   it should "default to value of automated morphological parsing for lexical disambiguation" in {
     val urn = "urn:cts:greekLit:tlg5026.msAil.hmt:12.F20.comment"
     val xml  = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> βαρεῖ, </p></div>"""
     val tokenV = TeiReader.teiToTokens(urn, xml)
     val token1 = tokenV(0)._2
     assert(token1.lexicalDisambiguation == "Automated disambiguation")

   }
   it should "default to value of direct voice for discourse category" in {
     val urn = "urn:cts:greekLit:tlg5026.msAil.hmt:12.F20.comment"
       val xml  = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> βαρεῖ, </p></div>"""
     val tokenV = TeiReader.teiToTokens(urn, xml)
     val token1 = tokenV(0)._2
     assert(token1.discourse == DirectVoice)
   }
   it should "default to no alternate readings" in {
     val urn = "urn:cts:greekLit:tlg5026.msAil.hmt:12.F20.comment"
       val xml  = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> βαρεῖ, </p></div>"""
     val tokenV = TeiReader.teiToTokens(urn, xml)
     val token1 = tokenV(0)._2
     assert(token1.alternateReading.alternateCategory == Original)
     assert(token1.alternateReading.reading.size == 0)
   }
   it should "default to no errors recorded" in {
     val urn = "urn:cts:greekLit:tlg5026.msAil.hmt:12.F20.comment"
       val xml  = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> βαρεῖ, </p></div>"""
     val tokenV = TeiReader.teiToTokens(urn, xml)
     val token1 = tokenV(0)._2
     assert(token1.errors.size == 0)
   }

   // test non-default editorial status
  it should "record editorial status for readings of TEI unclear element as unclear" in {
    val urn = "urn:cts:greekLit:tlg5026.msAext.hmt:15.8.comment"
    val xml =  """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> <w> <unclear> ἔν</unclear> θ'</w> εἴη</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val mixedReadings = tokenV(0)._2.readings
    assert(mixedReadings(0).status == Unclear)
    assert(mixedReadings(1).status == Clear)
  }




  // lexical category
  it should "categorize TEI num content as numeric lexical category" in {
    val urn = "urn:cts:greekLit:tlg5026.msAil.hmt:12.D5.comment"
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> εἰς <num value="5"> ε</num> τάξεις</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val numToken = tokenV(1)._2
    assert(numToken.lexicalCategory == NumericToken)
  }

  it should "categorize TEI rs content when @type = 'waw' as string literal lexical category" in {
    val urn = "urn:cts:greekLit:tlg5026.msAint.hmt:18.49.comment"
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> ἕξω τοῦ <rs type="waw"> ϊ</rs> τὸ <q> εστήκει</q> αἱ <persName n="urn:cite:hmt:pers.pers16"> <choice> <abbr> Ἀρισταρχ</abbr> <expan> Ἀριστάρχου</expan></choice></persName> ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val waw = tokenV(2)._2
    assert(waw.lexicalCategory == LiteralToken)
  }

  it should "categorize TEI sic content as unparseable lexical category" in {
    val urn = "urn:cts:greekLit:tlg5026.msAint.hmt:18.47.comment"
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> ἐν τῇ <sic> Μασσαλέωτικῆ</sic> <q> <sic> είμα</sic> τ' ἔχε</q> ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val sicToken = tokenV(2)._2
    assert(sicToken.lexicalCategory == Unintelligible)
  }


  // language
  it should "use xml:lang attribute of TEI foreign element for language" in {
    val urn = "urn:cts:greekLit:tlg5026.msA.hmt:1.39.comment"
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> ἐπίθετον <persName n="urn:cite:hmt:pers.pers40"> Ἀπόλλωνος</persName> · <placeName n="urn:cite:hmt:place.place17"> Σμίνθος</placeName> γὰρ τόπος τῆς <placeName n="urn:cite:hmt:place.place18"> Τρῳαδος</placeName> ἐν ᾧ ϊερὸν <persName n="urn:cite:hmt:pers.pers40"> Ἀπόλλωνος</persName> ἀπο αἰτιας τῆσδε ἐν <placeName n="urn:cite:hmt:place.place19"> Χρυσῃ</placeName> πόλει τῆς <placeName n="urn:cite:hmt:place.place20"> Μυσίας</placeName> <persName n="urn:cite:hmt:pers.pers56"> Κρίνις</persName> τίς ἱερεὺς τοῦ κεῖθι <persName n="urn:cite:hmt:pers.pers40"> Ἀπόλλωνος</persName> τούτου ὀργισθεὶς ὁ θεὸς ἔπεμψε τοῖς αγροῖς μυιας οἵτινες τοὺς καρποὺς ἐλυμαίνοντο· βουληθεὶς δέ ποτε ὁ θεὸς αὐτῷ καταλλαγῆναι προς <persName n="urn:cite:hmt:pers.pers57"> Όρδην</persName> τὸν ἀρχιβουκόλον αὐτοῦ παρεγένετο· παρ ῷ ξενισθεὶς ὁ θεὸς ὑπέσχετο τὸ <seg type="word"> κακ <choice> <sic> ὸν</sic> <corr> ῶς</corr></choice></seg> ἀπαλλάξειν· καὶ δὴ παραχρῆμα τοξεύσας τοὺς μῦς, <w> δι <choice> <sic> ε</sic> <corr> έ</corr></choice> φθειρεν</w> ἀπαλλασόμενος οὖν ἐνετείλατο τὴν ἐπιφάνιαν <w> αὐτ <choice> <sic> οῦ</sic> <corr> ὴν</corr></choice></w> δηλῶσαι τῷ <persName n="urn:cite:hmt:pers.pers56"> Κρίνιδι</persName> . οὗ γενομένου ὁ <persName n="urn:cite:hmt:pers.pers56"> Κρίνις</persName> ἱερὸν ἱδρύσατο τῷ θεῷ <persName n="urn:cite:hmt:pers.pers40"> Σμινθέα</persName> αὐτὸν προσαγορεύσας, επειδὴ κατὰ τὴν εγχωριον αυτῶν διάλεκτον οἱ <w> μ <choice> <sic> υ</sic> <corr> ύ</corr></choice> ες</w> <foreign xml:lang="mysian"> σμίνθιοι</foreign> καλοῦνται ἡ ἱστορια παρὰ <persName n="urn:cite:hmt:pers.pers58"> Πολεμωνι</persName> ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val tokenum = 99
    val mysian = tokenV(tokenum)._2
    //println("Token " + tokenum + " = " )
    //println(mysian.columnString)
    assert (mysian.lang == "mysian")
  }


  // alternate reading
  it should "have no alternate reading strings when alternate reading category is Original" in pending

  it should "have 1 or more alternate reading strings when alternate reading category is not Original" in pending

  it should "categorize alternate category of TEI expan as restoration" in {
    val urn = "urn:cts:greekLit:tlg5026.msAint.hmt:19.hc_5.comment"
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> <choice> <abbr> ουτ</abbr> <expan> οὕτως</expan></choice> δια τοῦ <rs type="waw"> ο</rs> <q> ζεύγνυον</q> ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val expanAbbr = tokenV(0)._2
    assert(expanAbbr.alternateReading.alternateCategory == Restoration)
  }

  it should "have a single reading for TEI expan classed as restored" in {

    val urn = "urn:cts:greekLit:tlg5026.msAint.hmt:19.hc_5.comment"
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> <choice> <abbr> ουτ</abbr> <expan> οὕτως</expan></choice> δια τοῦ <rs type="waw"> ο</rs> <q> ζεύγνυον</q> ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val expanAbbr = tokenV(0)._2
    assert(expanAbbr.alternateReading.reading.size == 1)
    val rdg = expanAbbr.alternateReading.reading(0)
    assert (rdg.status == Restored)
  }



  it should "categorize alternate category of TEI add as multiform" in pending

  it should "read contents of add element as regular editorial readings" in pending

  it should "categorize alternate category of TEI reg as multiform" in {
    val urn = "urn:cts:greekLit:tlg5026.msAim.hmt:9.625.comment"
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> τὸ μάχης ἑκατέροις <choice> <orig> δύνασθαι</orig> <reg> δύναται</reg></choice> προς δίδοσθαι ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val origReg = tokenV(3)._2
    assert(origReg.alternateReading.alternateCategory == Multiform)
  }

  it should "read contents of reg element as regular editorial readings" in {
    val urn = "urn:cts:greekLit:tlg5026.msAim.hmt:9.625.comment"
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> τὸ μάχης ἑκατέροις <choice> <orig> δύνασθαι</orig> <reg> δύναται</reg></choice> προς δίδοσθαι ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val regAlternative = tokenV(3)._2.alternateReading.reading
    assert(regAlternative(0).status == Clear)
  }

  it should "categorize alternate category of TEI corr as correction" in {
    val urn = "urn:cts:greekLit:tlg5026.msAil.hmt:10.2557.comment"
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> πρόφρων <choice> <sic> προμω</sic> <corr> προθύμως</corr></choice> · </p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val sicCorr = tokenV(1)._2
    assert(sicCorr.alternateReading.alternateCategory == Correction)
  }

  it should "read contents of corr element as regular editorial readings" in {
    val urn = "urn:cts:greekLit:tlg5026.msAil.hmt:10.2557.comment"
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> πρόφρων <choice> <sic> προμω</sic> <corr> προθύμως</corr></choice> · </p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)

    val corrAlternative = tokenV(1)._2.alternateReading.reading
    assert(corrAlternative(0).status == Clear)
  }

  // discourse category
  it should "categorize discourse of TEI cit as cited text" in {
      val urn = "urn:cts:greekLit:tlg5026.msAint.hmt:17.30.comment"
      val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> μακρὰ ἡ παρέκβασις καὶ <choice> <abbr> πάντ</abbr> <expan> πάντα</expan></choice> δια μέσου τὸ γὰρ ἑξῆς <cit> <ref type="urn"> urn:cts:greekLit:tlg0012.tlg001:17.611</ref> <q> <persName n="urn:cite:hmt:pers.pers1070"> Κοίρανον</persName></q></cit> , <cit> <ref type="urn"> urn:cts:greekLit:tlg0012.tlg001:17.617</ref> <q> βάλ' <choice> <abbr> υπ</abbr> <expan> ὑπὸ</expan></choice> γναθμοῖο</q></cit> ⁑</p></div>"""
      val tokenV = TeiReader.teiToTokens(urn, xml)
      val cited = tokenV(10)._2
      assert(cited.discourse == CitedText)
  }
  it should "record reference of cited text" in {
    val urn = "urn:cts:greekLit:tlg5026.msAint.hmt:17.30.comment"
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> μακρὰ ἡ παρέκβασις καὶ <choice> <abbr> πάντ</abbr> <expan> πάντα</expan></choice> δια μέσου τὸ γὰρ ἑξῆς <cit> <ref type="urn"> urn:cts:greekLit:tlg0012.tlg001:17.611</ref> <q> <persName n="urn:cite:hmt:pers.pers1070"> Κοίρανον</persName></q></cit> , <cit> <ref type="urn"> urn:cts:greekLit:tlg0012.tlg001:17.617</ref> <q> βάλ' <choice> <abbr> υπ</abbr> <expan> ὑπὸ</expan></choice> γναθμοῖο</q></cit> ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val cited = tokenV(10)._2
    //println("CITED: \n" + cited.columnString )
    //assert(cited.externalSource.size == "urn:cts:greekLit:tlg0012.tlg001:17.611".size)
    assert(cited.externalSource == "urn:cts:greekLit:tlg0012.tlg001:17.611")
  }
  it should "categorize discourse of TEI q outside of cit as quoted language" in pending
/*
urn:cts:greekLit:tlg5026.msAint.hmt:19.hc_5.comment#1646#1645#1647#/tei:TEI/tei:text/tei:body/tei:div[@n = '19']/tei:div[@n = 'hc_5']#<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> <choice> <abbr> ουτ</abbr> <expan> οὕτως</expan></choice> δια τοῦ <rs type="waw"> ο</rs> <q> ζεύγνυον</q> ⁑</p></div>
)
*/

  it should "add URL from ref element when discourse category is cited text" in pending

  // lexical disambiguation
  it should "categorize lexical disambiguation of TEI num as automated numerical parsing" in {
    val urn = "urn:cts:greekLit:tlg5026.msAil.hmt:12.D5.comment"
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> εἰς <num value="5"> ε</num> τάξεις</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val numToken = tokenV(1)._2
    assert (numToken.lexicalDisambiguation == "Automated numeric parsing")
  }

  it should "use n attribute of TEI persName element for lexical disambiguation" in {
    val urn = "urn:cts:greekLit:tlg5026.msAint.hmt:19.hc_3.comment"
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> καὶ <q> πόρε <persName n="urn:cite:hmt:pers.pers115"> Xείρων</persName></q> ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val pn = tokenV(2)._2
    assert(pn.lexicalDisambiguation == "urn:cite:hmt:pers.pers115")
  }

  it should "use  n attribute of TEI placeName element for lexical disambiguation" in {
    val urn = "urn:cts:greekLit:tlg5026.msAint.hmt:15.41.comment"
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> <choice> <abbr> οτ</abbr> <expan> ὅτι</expan></choice> θηλυκῶς τὴν <placeName n="urn:cite:hmt:place.place6"> Ἴλιον</placeName></p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val troy = tokenV(3)._2
    assert(troy.lexicalDisambiguation == "urn:cite:hmt:place.place6")
  }

  it should "use n attribute of TEI rs element for lexical disambiguation type attribute is ethnic" in {
    val urn = "urn:cts:greekLit:tlg5026.msAint.hmt:18.14.comment"
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> τῶν τοις <rs n="urn:cite:hmt:place.place6" type="ethnic"> Τρωσὶ</rs> συμμαχούντων ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val trojans = tokenV(2)._2
    assert(trojans.lexicalDisambiguation == "urn:cite:hmt:place.place6")
  }
}

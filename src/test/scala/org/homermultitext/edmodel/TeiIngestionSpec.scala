package org.homermultitext.edmodel
import org.scalatest._
import scala.xml._

import edu.holycross.shot.cite._

class TeiIngestionSpec extends FlatSpec {

  // test structure of result
  "The TeiReader object"  should "convert well-formed HMT TEI to a Vector of TokenAnalysis objects" in   {
    val xml = """<div type="scholion" n="hc_5" xmlns="http://www.tei-c.org/ns/1.0"><div type="lemma"> <p/></div><div type="comment"> <p> <choice> <abbr> ουτ</abbr> <expan> οὕτως</expan></choice> δια τοῦ <rs type="waw"> ο</rs> <q> ζεύγνυον</q> ⁑</p></div></div>"""
    val urn = CtsUrn("urn:cts:greekLit:tlg5026.msAint.hmt:19.hc_5")
    val analysisV = TeiReader.teiToTokens(urn, xml)
    val firstEntry = analysisV(0)

    firstEntry match {
      case ta: TokenAnalysis => assert( ta.analysis.analysis == CiteUrn("urn:cite:hmt:va_schAint_tkns.tkn1") )
      case _ => fail("Object is not a TokenAnalysis")
    }
  }

  it should "record the context of this analysis in the analyzed text's canonical citation scheme" in {
    val xml = """<div type="scholion" n="hc_5" xmlns="http://www.tei-c.org/ns/1.0"><div type="lemma"> <p/></div><div type="comment"> <p> <choice> <abbr> ουτ</abbr> <expan> οὕτως</expan></choice> δια τοῦ <rs type="waw"> ο</rs> <q> ζεύγνυον</q> ⁑</p></div></div>"""
    val urn = CtsUrn("urn:cts:greekLit:tlg5026.msAint.hmt:19.hc_5")
    val analysisV = TeiReader.teiToTokens(urn, xml)
    val firstEntry = analysisV(0)
    assert (firstEntry.textNode == CtsUrn("urn:cts:greekLit:tlg5026.msAint.hmt:19.hc_5"))
  }
  it should "compose a CtsUrn situating this token in an analytical edition " in {
    val xml = """<div type="scholion" n="hc_5" xmlns="http://www.tei-c.org/ns/1.0"><div type="lemma"> <p/></div><div type="comment"> <p> <choice> <abbr> ουτ</abbr> <expan> οὕτως</expan></choice> δια τοῦ <rs type="waw"> ο</rs> <q> ζεύγνυον</q> ⁑</p></div></div>"""
    val urn = CtsUrn("urn:cts:greekLit:tlg5026.msAint.hmt:19.hc_5")
    val analysisV = TeiReader.teiToTokens(urn, xml)
    val firstEntry = analysisV(0)
    assert (firstEntry.analysis.editionUrn == CtsUrn("urn:cts:greekLit:tlg5026.msAint.hmt_tkns:19.hc_5.1"))
  }
  it should "index compute a subreference value for token string within the text of the source element" in pending
}
/*
  // test tokenization
  it should "tokenize on white space by default" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAil.hmt:1.1303.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> ἢ απαρνησαι</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    assert(tokenV.size == 2)
  }

  it should "ignore white space within TEI w elements" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAext.hmt:15.8.comment")
    val xml =  """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> <w> <unclear> ἔν</unclear> θ'</w> εἴη</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    assert(tokenV.size == 2)
    assert (tokenV(0)._2.readings.size == 2)
  }

  it should "ensure that w element wraps both clear and unclear readings" in pending

  it should "ensure that w element wraps no TEI elements other than unclear" in pending

  it should "treat individual punctuation characters as tokens of lexical category punctuation" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAil.hmt:12.F20.comment")
    val xml  = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> βαρεῖ, </p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val punctToken = tokenV(1)._2
    assert (punctToken.lexicalCategory == Punctuation)
  }

  it should "note instances of invalid element names" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msA.hmt:1.39.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> ὁ θεὸς ὑπέσχετο τὸ <seg type="word"> κακ <choice> <sic> ὸν</sic> <corr> ῶς</corr></choice></seg> ἀπαλλάξειν</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val correct = tokenV(0)._2
    val incorrect = tokenV(4)._2
    assert(correct.errors.size == 0)
    assert(incorrect.errors.size == 1)
  }

  // test all default values:
  it should "default to editorial status of clear" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAil.hmt:12.F20.comment")
    val xml  = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> βαρεῖ, </p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val token1readings = tokenV(0)._2.readings
    assert(token1readings(0).status == Clear)
   }

   it should "default to value of grc for language" in {
     val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAil.hmt:12.F20.comment")
       val xml  = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> βαρεῖ, </p></div>"""
     val tokenV = TeiReader.teiToTokens(urn, xml)
     val token1 = tokenV(0)._2
     assert(token1.lang == "grc")
   }

   it should "default to lexical category of lexical item" in {
     val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAil.hmt:12.F20.comment")
       val xml  = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> βαρεῖ, </p></div>"""
     val tokenV = TeiReader.teiToTokens(urn, xml)
     val token1 = tokenV(0)._2
     assert(token1.lexicalCategory == LexicalToken)
   }

   it should "default to value of automated morphological parsing for lexical disambiguation" in {
     val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAil.hmt:12.F20.comment")
     val xml  = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> βαρεῖ, </p></div>"""
     val tokenV = TeiReader.teiToTokens(urn, xml)
     val token1 = tokenV(0)._2
     assert(token1.lexicalDisambiguation == CiteUrn("urn:cite:hmt:disambig.lexical.v1"))
   }

   it should "default to value of direct voice for discourse category" in {
     val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAil.hmt:12.F20.comment")
       val xml  = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> βαρεῖ, </p></div>"""
     val tokenV = TeiReader.teiToTokens(urn, xml)
     val token1 = tokenV(0)._2
     assert(token1.discourse == DirectVoice)
   }

   it should "default to no alternate readings" in {
     val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAil.hmt:12.F20.comment")
       val xml  = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> βαρεῖ, </p></div>"""
     val tokenV = TeiReader.teiToTokens(urn, xml)
     val token1 = tokenV(0)._2
     //assert(token1.alternateReading.alternateCategory == Original)
     //assert(token1.alternateReading.reading.size == 0)
     token1.alternateReading match {
       case Some(alt) => {
         fail("Should not have found alternate reading for " + token1)
       }
       case None => assert (1 == 1)
     }
   }

   it should "default to no errors recorded" in {
     val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAil.hmt:12.F20.comment")
       val xml  = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> βαρεῖ, </p></div>"""
     val tokenV = TeiReader.teiToTokens(urn, xml)
     val token1 = tokenV(0)._2
     assert(token1.errors.size == 0)
   }

   // test non-default editorial status
  it should "record editorial status for readings of TEI unclear element as unclear" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAext.hmt:15.8.comment")
    val xml =  """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> <w> <unclear> ἔν</unclear> θ'</w> εἴη</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val mixedReadings = tokenV(0)._2.readings
    assert(mixedReadings(0).status == Unclear)
    assert(mixedReadings(1).status == Clear)
  }

  // lexical category
  it should "categorize TEI num content as numeric lexical category" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAil.hmt:12.D5.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> εἰς <num value="5"> ε</num> τάξεις</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val numToken = tokenV(1)._2
    assert(numToken.lexicalCategory == NumericToken)
  }

  it should "categorize TEI rs content when @type = 'waw' as string literal lexical category" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAint.hmt:18.49.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> ἕξω τοῦ <rs type="waw"> ϊ</rs> τὸ <q> εστήκει</q> αἱ <persName n="urn:cite:hmt:pers.pers16"> <choice> <abbr> Ἀρισταρχ</abbr> <expan> Ἀριστάρχου</expan></choice></persName> ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val waw = tokenV(2)._2
    assert(waw.lexicalCategory == LiteralToken)
  }

  it should "categorize TEI sic content as unparseable lexical category" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAint.hmt:18.47.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> ἐν τῇ <sic> Μασσαλέωτικῆ</sic> <q> <sic> είμα</sic> τ' ἔχε</q> ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val sicToken = tokenV(2)._2
    assert(sicToken.lexicalCategory == Unintelligible)
  }

  // language
  it should "use xml:lang attribute of TEI foreign element for language" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msA.hmt:1.39.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> ἐπίθετον <persName n="urn:cite:hmt:pers.pers40"> Ἀπόλλωνος</persName> · <placeName n="urn:cite:hmt:place.place17"> Σμίνθος</placeName> γὰρ τόπος τῆς <placeName n="urn:cite:hmt:place.place18"> Τρῳαδος</placeName> ἐν ᾧ ϊερὸν <persName n="urn:cite:hmt:pers.pers40"> Ἀπόλλωνος</persName> ἀπο αἰτιας τῆσδε ἐν <placeName n="urn:cite:hmt:place.place19"> Χρυσῃ</placeName> πόλει τῆς <placeName n="urn:cite:hmt:place.place20"> Μυσίας</placeName> <persName n="urn:cite:hmt:pers.pers56"> Κρίνις</persName> τίς ἱερεὺς τοῦ κεῖθι <persName n="urn:cite:hmt:pers.pers40"> Ἀπόλλωνος</persName> τούτου ὀργισθεὶς ὁ θεὸς ἔπεμψε τοῖς αγροῖς μυιας οἵτινες τοὺς καρποὺς ἐλυμαίνοντο· βουληθεὶς δέ ποτε ὁ θεὸς αὐτῷ καταλλαγῆναι προς <persName n="urn:cite:hmt:pers.pers57"> Όρδην</persName> τὸν ἀρχιβουκόλον αὐτοῦ παρεγένετο· παρ ῷ ξενισθεὶς ὁ θεὸς ὑπέσχετο τὸ <seg type="word"> κακ <choice> <sic> ὸν</sic> <corr> ῶς</corr></choice></seg> ἀπαλλάξειν· καὶ δὴ παραχρῆμα τοξεύσας τοὺς μῦς, <w> δι <choice> <sic> ε</sic> <corr> έ</corr></choice> φθειρεν</w> ἀπαλλασόμενος οὖν ἐνετείλατο τὴν ἐπιφάνιαν <w> αὐτ <choice> <sic> οῦ</sic> <corr> ὴν</corr></choice></w> δηλῶσαι τῷ <persName n="urn:cite:hmt:pers.pers56"> Κρίνιδι</persName> . οὗ γενομένου ὁ <persName n="urn:cite:hmt:pers.pers56"> Κρίνις</persName> ἱερὸν ἱδρύσατο τῷ θεῷ <persName n="urn:cite:hmt:pers.pers40"> Σμινθέα</persName> αὐτὸν προσαγορεύσας, επειδὴ κατὰ τὴν εγχωριον αυτῶν διάλεκτον οἱ <w> μ <choice> <sic> υ</sic> <corr> ύ</corr></choice> ες</w> <foreign xml:lang="mysian"> σμίνθιοι</foreign> καλοῦνται ἡ ἱστορια παρὰ <persName n="urn:cite:hmt:pers.pers58"> Πολεμωνι</persName> ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val tokenum = 99
    val mysian = tokenV(tokenum)._2
    //println("Token " + tokenum + " = " )
    //println(mysian.columnString)
    assert (mysian.lang == "mysian")
  }

  // alternate reading
  it should "have no alternate reading by default" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAint.hmt:18.47.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> ἐν τῇ <sic> Μασσαλέωτικῆ</sic> <q> <sic> είμα</sic> τ' ἔχε</q> ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val tkn = tokenV(0)._2
    tkn.alternateReading match {
      case Some(alt) => fail("Should not have found alternate reading for " + tkn)
      case None => assert (1 == 1)
    }
  }

  it should "categorize alternate category of TEI expan as restoration" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAint.hmt:19.hc_5.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> <choice> <abbr> ουτ</abbr> <expan> οὕτως</expan></choice> δια τοῦ <rs type="waw"> ο</rs> <q> ζεύγνυον</q> ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val expanAbbr = tokenV(0)._2
    expanAbbr.alternateReading match {
      case Some(alt) => assert (alt.alternateCategory == Restoration)
      case None => fail("No alternate reading found for " + tokenV(1))
    }
  }

  it should "have a single reading for TEI expan classed as restored" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAint.hmt:19.hc_5.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> <choice> <abbr> ουτ</abbr> <expan> οὕτως</expan></choice> δια τοῦ <rs type="waw"> ο</rs> <q> ζεύγνυον</q> ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val expanAbbr = tokenV(0)._2
    expanAbbr.alternateReading match {
      case Some(alt) => {
        assert (alt.reading.size == 1)
        assert (alt.reading(0).status == Restored)
      }
      case None => fail("No alternate reading found for " + tokenV(1))
    }
  }

  it should "categorize alternate category of TEI add as multiform" in pending


  it should "read contents of add element as regular editorial readings" in pending

  it should "categorize alternate category of TEI reg as multiform" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAim.hmt:9.625.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> τὸ μάχης ἑκατέροις <choice> <orig> δύνασθαι</orig> <reg> δύναται</reg></choice> προς δίδοσθαι ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val origReg = tokenV(3)._2
    origReg.alternateReading match {
      case Some(alt) => assert (alt.alternateCategory == Multiform)
      case None => fail("No alternate reading found for " + tokenV(1))
    }
  }

  it should "read contents of reg element as regular editorial readings" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAim.hmt:9.625.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> τὸ μάχης ἑκατέροις <choice> <orig> δύνασθαι</orig> <reg> δύναται</reg></choice> προς δίδοσθαι ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    tokenV(3)._2.alternateReading match {
      case Some(alt) => assert (alt.reading(0).status == Clear)
      case None => fail("No alternate reading found for " + tokenV(1))
    }
  }

  it should "categorize alternate category of TEI corr as correction" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAil.hmt:10.2557.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> πρόφρων <choice> <sic> προμω</sic> <corr> προθύμως</corr></choice> · </p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val sicCorr = tokenV(1)._2
    sicCorr.alternateReading match {
      case Some(alt) => assert (alt.alternateCategory == Correction)
      case None => fail("No alternate reading found for " + tokenV(1))
    }
  }

  it should "read contents of corr element as regular editorial readings" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAil.hmt:10.2557.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> πρόφρων <choice> <sic> προμω</sic> <corr> προθύμως</corr></choice> · </p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    tokenV(1)._2.alternateReading match {
      case Some(alt) => assert (alt.reading(0).status == Clear)
      case None => fail("No alternate reading found for " + tokenV(1))
    }
  }

  // discourse category
  it should "categorize discourse of TEI cit as cited text" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAint.hmt:17.30.comment")
      val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> μακρὰ ἡ παρέκβασις καὶ <choice> <abbr> πάντ</abbr> <expan> πάντα</expan></choice> δια μέσου τὸ γὰρ ἑξῆς <cit> <ref type="urn"> urn:cts:greekLit:tlg0012.tlg001:17.611</ref> <q> <persName n="urn:cite:hmt:pers.pers1070"> Κοίρανον</persName></q></cit> , <cit> <ref type="urn"> urn:cts:greekLit:tlg0012.tlg001:17.617</ref> <q> βάλ' <choice> <abbr> υπ</abbr> <expan> ὑπὸ</expan></choice> γναθμοῖο</q></cit> ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val tokenN = 10
    val cited = tokenV(tokenN)._2
    assert(cited.discourse == QuotedText)
  }

  it should "record reference of external source when discourse type is cited text" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAint.hmt:17.30.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> μακρὰ ἡ παρέκβασις καὶ <choice> <abbr> πάντ</abbr> <expan> πάντα</expan></choice> δια μέσου τὸ γὰρ ἑξῆς <cit> <ref type="urn"> urn:cts:greekLit:tlg0012.tlg001:17.611</ref> <q> <persName n="urn:cite:hmt:pers.pers1070"> Κοίρανον</persName></q></cit> , <cit> <ref type="urn"> urn:cts:greekLit:tlg0012.tlg001:17.617</ref> <q> βάλ' <choice> <abbr> υπ</abbr> <expan> ὑπὸ</expan></choice> γναθμοῖο</q></cit> ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val cited = tokenV(10)._2
    assert(cited.discourse == QuotedText)
    assert(cited.externalSource == Some(CtsUrn( "urn:cts:greekLit:tlg0012.tlg001:17.611")))
  }

  it should "categorize discourse of TEI q outside of cit as quoted language" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAint.hmt:19.hc_5.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> <choice> <abbr> ουτ</abbr> <expan> οὕτως</expan></choice> δια τοῦ <rs type="waw"> ο</rs> <q> ζεύγνυον</q> ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val quoted = tokenV(4)._2
    assert(quoted.discourse == QuotedLanguage)
  }

  // lexical disambiguation
  it should "categorize lexical disambiguation of TEI num as automated numerical parsing" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAil.hmt:12.D5.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> εἰς <num value="5"> ε</num> τάξεις</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val numToken = tokenV(1)._2
    assert (numToken.lexicalDisambiguation == CiteUrn("urn:cite:hmt:disambig.numeric.v1"))
  }

  it should "use n attribute of TEI persName element for lexical disambiguation" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAint.hmt:19.hc_3.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> καὶ <q> πόρε <persName n="urn:cite:hmt:pers.pers115"> Xείρων</persName></q> ⁑</p></div>"""

    val tokenV = TeiReader.teiToTokens(urn, xml)
    val pn = tokenV(2)._2
    assert(pn.lexicalDisambiguation == CiteUrn("urn:cite:hmt:pers.pers115"))
  }

  it should "use  n attribute of TEI placeName element for lexical disambiguation" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAint.hmt:15.41.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> <choice> <abbr> οτ</abbr> <expan> ὅτι</expan></choice> θηλυκῶς τὴν <placeName n="urn:cite:hmt:place.place6"> Ἴλιον</placeName></p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val troy = tokenV(3)._2
    assert(troy.lexicalDisambiguation == CiteUrn("urn:cite:hmt:place.place6"))
  }

  it should "use n attribute of TEI rs element for lexical disambiguation type attribute is ethnic" in {
    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAint.hmt:18.14.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> τῶν τοις <rs n="urn:cite:hmt:place.place6" type="ethnic"> Τρωσὶ</rs> συμμαχούντων ⁑</p></div>"""
    val tokenV = TeiReader.teiToTokens(urn, xml)
    val trojans = tokenV(2)._2
    assert(trojans.lexicalDisambiguation == CiteUrn("urn:cite:hmt:place.place6"))
  }

  it should "read a tab-delimited two-column file and create a Vector of (urn,token) tuples" in {
    val fName = "src/test/resources/sample1-twocolumn.tsv"
    val tokens = TeiReader.fromTwoColumns(fName)
    // more than 150 tokens from 3 scholia
    assert (tokens.size > 150)
    assert (tokens.groupBy( _._1).size == 3)
  }

  it should "read a two-column file with specified  delimited and create a Vector of (urn,token) tuples" in {
    val fName = "src/test/resources/sample1-twocolumn-pound.txt"
    val separator = "#"
    val tokens = TeiReader.fromTwoColumns(fName, separator)
    // more than 150 tokens from 3 scholia
    assert (tokens.size > 150)
    assert (tokens.groupBy( _._1).size == 3)
  }


}*/

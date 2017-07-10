
// Given a CEX file of HMT scholia, write to file in CEX format a
// corpus of lemmata tokens.

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.orca._
import edu.holycross.shot.greek._
import org.homermultitext.edmodel._


import java.io.PrintWriter

println("\nCreating corpus for scholia...")
val scholia = CorpusSource.fromFile("src/test/resources/scholia8K.cex", "#")
println("\n\nTokenizing scholia...")
val tokensScholia = TeiReader.fromCorpus(scholia)
println("\n\nCreating diplomatic edition of scholia ...")
val diplScholia = DiplomaticEditionFactory.corpusFromTokens(tokensScholia)

new PrintWriter("diplscholia.cex") { write(diplScholia.to2colString("#")); close }


// get diplomatic lemmata
 val lemmataCorpus = Corpus(diplScholia.nodes.filter(_.urn.passageComponent.contains("lemma")))
 new PrintWriter("lemmata.cex") { write(lemmataCorpus.to2colString("#")); close }

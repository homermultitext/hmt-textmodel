//
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.orca._
import edu.holycross.shot.greek._
import org.homermultitext.edmodel._

val corpus = CorpusFileIO.fromFile("data/hmtarchive_2cols.tsv")
val mainScholia = corpus ~~ CtsUrn("urn:cts:greekLit:tlg5026.msA:")

val tokens = TeiReader.fromCorpus(Corpus(mainScholia))

val citationsList = tokens.filter(_.analysis.discourse == Citation).map (ta => ta.analysis.editionUrn.toString + "\t" + Reading.leidenize(ta.analysis.readings)).mkString("\n")

import java.io._
new PrintWriter("hmt-citations.tsv") { write(citationsList); close }

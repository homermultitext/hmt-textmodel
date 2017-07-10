
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.orca._
import edu.holycross.shot.greek._
import org.homermultitext.edmodel._


import java.io.PrintWriter


println("\nCreating corpus for Iliad text...")
val iliad = CorpusSource.fromFile("src/test/resources/iliad10k.cex", "#")

println("\n\nTokenizing Iliad text...")
val tokensIliad = TeiReader.fromCorpus(iliad)
println("\n\nCreating diplomatic edition of Iliad ...")
val diplIliad = DiplomaticEditionFactory.corpusFromTokens(tokensIliad)

new PrintWriter("diplomaticIliadTokens.cex") { write(diplIliad.to2colString("#")); close }


val zipped = diplIliad.nodes.zipWithIndex
val triple = zipped.map{ case (cn,i) => (cn.urn.passageComponent,cn,i) }


val reduced = triple.map { case (s,cn,i) => (s.split("[.]").dropRight(1).mkString("."),cn,i) }
val grouped = reduced.groupBy(_._1).values.toVector



def flattenIt(v: Vector[(String, edu.holycross.shot.ohco2.CitableNode, Int)]) = {
  val psg = v(0)._1
  val seq = v(0)._3
  val urn = CtsUrn(s"${v(0)._2.urn.dropPassage}${psg}")
  val cnodes = v.map(_._2)
  (seq, CitableNode(urn,   cnodes.map(_.text).mkString(" ")))
}

val diplFolded =  Corpus(grouped.map(flattenIt(_)).sortBy(_._1).map( _._2))
new PrintWriter("diplomaticIliadLines.cex") { write(diplFolded.to2colString("#")); close }

/*
 val flatter = sortedInner.flatMap( v => v.map{ case (s,cn,i) => (i,cn,s) } ).sortBy(_._1)
// flatter.map { case (i,n,s) => (n,s) }
val vOfNodes =  flatter.map { case (i,n,s) => (n.text,n.urn.dropPassage + s) }
*/
/*
tups.map{ case (psg,cn ) => psg.split("[.]".dropRight(1) }

val tups = iliad.nodes.map( cn => (cn.urn.passageComponent, cn))

split("[.]").dropRight(1).mkString(".")


val reduced = trip.map { case (s,cn,i) => (s.split("[.]").dropRight(1).mkString("."),cn,i) }








*/

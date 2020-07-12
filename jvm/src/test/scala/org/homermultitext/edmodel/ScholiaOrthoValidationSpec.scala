package org.homermultitext.edmodel

import edu.holycross.shot.greek._
import org.scalatest.FlatSpec

import edu.holycross.shot.scm._
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.citevalidator._



class ScholiaOrthoValidationSpec extends FlatSpec {

  val f = "jvm/src/test/resources/hmt-reduced.cex"
  val lib = CiteLibrarySource.fromFile(f)
  val corpus = lib.textRepository.get.corpus
  val validator  = ScholiaOrthographyValidator(lib)
  val pg = Cite2Urn("urn:cite2:hmt:msA.v1:292v")

  "An ScholiaOrthographyValidator" should "validate a citable node" in {
    val u = CtsUrn("urn:cts:greekLit:tlg5026.msA.hmt:1.3.comment")
    val txt = "ὅτι κατὰ τὴν ποιητικὴν ἤτοι άδειαν ἠ συνήθειαν λαμβάνει τὰ προστακτικὰ ἀντι εὐκτικῶν· καὶ γὰρ Ἡσίοδός φησι· δεῦτε δὴ ἐννέπετε , καὶ Πίνδαρος · μαντεύο Μοῦσα , καὶ Ἀντίμαχος ὁ Κολοφώνιος. ἐννέπετε Κρονίανος Διὸς μεγάλοιο θύγατρες · δευτερον δὲ ὅτι οὐ κατὰ ἀλήθειαν ταῖς Μούσαις ἐπιτάσσουσιν , ἀλλ' ἑαυτοῖς ⁑"

      val cn =  CitableNode(u, txt)

      val rslts = validator.validate(cn)
      val tg = TestResultGroup("Validation of LiteraryGreekString orthography", rslts)
      println(tg.markdown)
      //println("TEXT TO VALIDATE: " + cn.text)
      //println(" GOOD / TOTAL: " + rslts.filter(_.success).size + " / " + rslts.size)
      //println("BAD: " + rslts.filterNot(_.success))
      //println("MADE a validator with corpu sof " + corpus.size + " nodes")

  }

  it should "validate a CiteLibary" in pending

  it should "validate a DSE surface" in {
    val rslts = validator.validate(pg)
  }

  it should "format a verification report for a surface" in  pending /*{
    val verify = validator.verify(pg)
    println(verify)
  }*/

}

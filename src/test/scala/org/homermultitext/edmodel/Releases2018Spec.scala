package org.homermultitext.edmodel

import org.scalatest._
import scala.xml._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._


class Releases2018Spec extends FlatSpec  {

  "A TEI Reader" should "handle archival material from 2018 releases" in pending
  // testing big data sets in unit tests is stupid.
  /*{

    val catalog = "src/test/resources/hmt-2018/ctscatalog.cex"
    val citation = "src/test/resources/hmt-2018/citationconfig.cex"

    val repo = TextRepositorySource.fromFiles(catalog,citation,"src/test/resources/hmt-2018")
    println("Made repo with " + repo.corpus.size + " nodes.")

    val tokens = TeiReader.fromCorpus(repo.corpus)
    println("Yielded " + tokens.size + " tokens")
  }*/

}

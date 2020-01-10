package org.homermultitext.edmodel

import org.homermultitext.hmtcexbuilder._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import edu.holycross.shot.scm._
import edu.holycross.shot.cex._
import edu.holycross.shot.xmlutils._

import java.io.PrintWriter
import scala.io._

import org.scalatest.FlatSpec

class HmtArchiveSpec extends FlatSpec {
  val scholiaXml = "archive/scholia"
  val iliadXml = "archive/iliad"
  // 2. target directory for intermeidate composite xml output
  val scholiaComposites = "archive/scholia-xml-composites"
  val iliadComposites = "archive/iliad-xml-composites"
  // 3. target directory for publishable CEX editions of texts
  val cexEditions = "archive/editions"




  /**  Compose editions of Iliad.  This includes
  * an archival XML edition in CEX format;  and
  * a pure diplomatic edition in CEX format.
  */
  def iliad : Unit= {
    // revisit this for making multiple Iliads...
    val fileBase = "va_iliad_"
    println("Creating editions of Iliad...")

    // create temporary composite XML file from source
    // documents organized by book:
    IliadComposite.composite(iliadXml, iliadComposites)
    val catalog = s"${iliadComposites}/ctscatalog.cex"
    val citation = s"${iliadComposites}/citationconfig.cex"
    val repo = TextRepositorySource.fromFiles(catalog,citation,iliadComposites)
    // write CEX-formatted version of archival XML:
    new PrintWriter(s"${iliadComposites}/va_iliad_xml.cex") { write(repo.cex("#"));close }

    val diplIliad =  DiplomaticReader.edition(repo.corpus)
    val diplHeader = "\n\n#!ctscatalog\nurn#citationScheme#groupName#workTitle#versionLabel#exemplarLabel#online#lang\nurn:cts:greekLit:tlg0012.tlg001.msA:#book,line#Homeric epic#Iliad#HMT project diplomatic edition##true#grc\n\n#!ctsdata\n"


    val tidyDipl = diplIliad.cex("#").replaceAll("[\t ]+", " ")

    new PrintWriter(s"${cexEditions}/va_iliad_diplomatic.cex") { write(diplHeader +  tidyDipl);close }
  }


  def scholia: Unit = {}

  "All this" should "build an HMT library" in pending/* {

  IliadComposite.composite(iliadXml, iliadComposites)
    val catalog = s"${iliadComposites}/ctscatalog.cex"
    val citation = s"${iliadComposites}/citationconfig.cex"
    val repo = TextRepositorySource.fromFiles(catalog,citation,iliadComposites)
    val dipl =  DiplomaticReader.edition(repo.corpus)
  }*/

  it should "work encapsulated as a function" in pending /*{
    iliad
  }*/


  it should "work on scholia" in {

    println("Creating composite XML editions of scholia...")
    ScholiaComposite.composite(scholiaXml, scholiaComposites)
    val catalog = s"${scholiaComposites}/ctscatalog.cex"
    val citation = s"${scholiaComposites}/citationconfig.cex"

    val repo = TextRepositorySource.fromFiles(catalog,citation,scholiaComposites)
    val scholiaNodes = repo.corpus.nodes.filterNot(_.urn.passageComponent.endsWith("ref"))

    val scholiaRepo = TextRepository( Corpus(scholiaNodes), repo.catalog)
    val corpus = scholiaRepo.corpus
    val scholiaDocs = corpus.nodes.map(_.urn.dropPassage).distinct
    for (s <- scholiaDocs) {
      if (s.work != "msAextra") {
        println("Get subcorpus for " + s)
        val subcorpusAll = corpus ~~ s

        val cleanNodes = subcorpusAll.nodes.filterNot(n => TextReader.collectText(n.text).trim.isEmpty)
        println("Clean nodes: " + cleanNodes.size)
        val subcorpus = Corpus(cleanNodes)
        println("Got " + subcorpus.size + " scholia.")
        val diplSubcorpus = DiplomaticReader.edition(subcorpus)
        /*
          val diplEdition = DiplomaticEditionFactory.corpusFromTokens(tokens)
          val diplByScholion = diplEdition.exemplarToVersion("dipl")

          val diplHeader = s"\n\n#!ctscatalog\nurn#citationScheme#groupName#workTitle#versionLabel#exemplarLabel#online#lang\nurn:cts:greekLit:tlg5026.${siglum}.dipl:#book,scholion, section#Scholia to the Iliad#Scholia ${siglum} in the Venetus A#HMT project diplomatic edition##true#grc\n\n#!ctsdata\n"
          new PrintWriter(s"${editionsDir}/${siglum}_diplomatic.cex") { write(diplHeader + diplByScholion.cex("#"));close }
        */
      }
    }
  }

  //////// THIS NEEDS REWRITING
/*
  val tokens = TeiReader.fromCorpus(scholiaRepo.corpus)

  // Compute corrigenda for each scholia document, and
  // generate automatically derived editions:

  for (s <- scholiaDocs) {
    if (s != "msAextra") {
    //if (s == "msAil") {
      //println("Get tokens for " + s)
      val subCorpusTokens = tokens.filter(_.textNode.work == s)
      publishScholiaCorpus(subCorpusTokens, s, cexEditions)
      indexAuthlists(subCorpusTokens, s, cexEditions)
    }
  }
  // Write index file indexing scholia to Iliad passage they
  // comment on:
  val refNodes = repo.corpus.nodes.filter(_.urn.passageComponent.endsWith("ref"))
  indexScholiaCommentary (refNodes, cexEditions)

  // Now index all personal names and place names...
  */

}

package org.homermultitext.edmodel

import edu.holycross.shot.citevalidator._
import edu.holycross.shot.mid.markupreader._
import edu.holycross.shot.mid.orthography._


import scala.xml._
import scala.io.Source

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter



object DiplomaticReader extends MidMarkupReader with LogSupport {
  Logger.setDefaultLogLevel(LogLevel.DEBUG)

  def editionType: MidEditionType = HmtDiplomaticEdition

  // required by MidMarkupReader
  def recognizedTypes: Vector[MidEditionType] =  Vector(HmtDiplomaticEdition)

  // required by MidMarkupReader
  def editedNode(cn: CitableNode, edType: MidEditionType = HmtDiplomaticEdition): CitableNode = {
    val editedUrn = cn.urn //.addVersion(cn.urn.version + "_dipl")
    debug("dipl for: " + editedUrn)
    //println("dipl for: " + editedUrn)
    val tokens = TeiReader.analyzeCitableNode(cn)
    if (tokens.isEmpty) {
      val msg = "for NODE " + cn + " : no tokens."
      error(msg)
    }
    val txtBuilder = StringBuilder.newBuilder

    txtBuilder.append(tokens.head.readWithDiplomatic)

    for (n <- tokens.tail) {
      if (n.readWithDiplomatic == PunctuationToken) {
        txtBuilder.append(n.readWithDiplomatic)
      } else {
        txtBuilder.append(" " + n.readWithDiplomatic)
      }
    }
    CitableNode(editedUrn, txtBuilder.toString)
  }

}

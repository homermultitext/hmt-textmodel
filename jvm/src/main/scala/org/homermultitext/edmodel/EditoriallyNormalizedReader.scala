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


object EditoriallyNormalizedReader extends MidMarkupReader with LogSupport {

  def editionType: MidEditionType = HmtDiplomaticEdition

  // required by MidMarkupReader
  def recognizedTypes: Vector[MidEditionType] =  Vector(HmtDiplomaticEdition)

  // required by MidMarkupReader
  def editedNode(cn: CitableNode, editionType: MidEditionType = HmtDiplomaticEdition): CitableNode = {
    val editedUrn = cn.urn.addVersion(cn.urn.version + "_dipl")
    val tokens = TeiReader.analyzeCitableNode(cn)

    val txtBuilder = StringBuilder.newBuilder
    txtBuilder.append(tokens.head.readWithAlternate)

    for (n <- tokens.tail) {
      if (n.readWithAlternate == PunctuationToken) {
        txtBuilder.append(n.readWithAlternate)
      } else {
        txtBuilder.append(" " + n.readWithAlternate)
      }
    }
    CitableNode(editedUrn, txtBuilder.toString)
  }

}

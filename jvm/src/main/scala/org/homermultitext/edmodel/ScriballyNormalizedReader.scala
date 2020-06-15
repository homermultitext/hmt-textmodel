package org.homermultitext.edmodel

import edu.holycross.shot.citevalidator._
import edu.holycross.shot.mid.markupreader._
import edu.holycross.shot.mid.orthography._

import scala.xml._
import scala.io.Source

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._


object ScriballyNormalizedReader extends MidMarkupReader {

  def editionType: MidEditionType = HmtDiplomaticEdition

  // required by MidMarkupReader
  def recognizedTypes: Vector[MidEditionType] =  Vector(HmtDiplomaticEdition)

  // required by MidMarkupReader
  def editedNode(cn: CitableNode, editionType: MidEditionType =  HmtDiplomaticEdition): CitableNode = {
    val editedUrn = cn.urn.addVersion(cn.urn.version + "_dipl")
    val tokens = TeiReader.analyzeCitableNode(cn)

    val txtBuilder = StringBuilder.newBuilder
    txtBuilder.append(tokens.head.readWithScribal)

    for (n <- tokens.tail) {
      if (n.readWithDiplomatic == PunctuationToken) {
        txtBuilder.append(n.readWithScribal)
      } else {
        txtBuilder.append(" " + n.readWithScribal)
      }
    }
    CitableNode(editedUrn, txtBuilder.toString)
  }

}

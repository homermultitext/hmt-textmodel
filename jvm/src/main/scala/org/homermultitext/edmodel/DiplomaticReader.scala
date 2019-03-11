package org.homermultitext.edmodel

import edu.holycross.shot.mid.validator._


import scala.xml._
import scala.io.Source

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._


object DiplomaticReader extends MidMarkupReader {

  def editionType: MidEditionType = HmtDiplomaticEdition

  // required by MidMarkupReader
  def recognizedTypes: Vector[MidEditionType] =  Vector(HmtDiplomaticEdition)

  // required by MidMarkupReader
  def editedNode(cn: CitableNode): CitableNode = {
    val editedUrn = cn.urn.addVersion(cn.urn.version + "_dipl")
    val tokens = TeiReader.analyzeCitableNode(cn)

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

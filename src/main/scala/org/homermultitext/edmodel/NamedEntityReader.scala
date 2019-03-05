package org.homermultitext.edmodel

import edu.holycross.shot.mid.validator._


import scala.xml._
import scala.io.Source

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._


object NamedEntityReader {


  def neNode  (n: CitableNode) : CitableNode  = {
    val srcUrn = n.urn
    val editedUrn = srcUrn.addVersion( srcUrn.version + "_" + HmtNamedEntityEdition.versionId)

    // decide what it should do.  Text content is URN for NE?
    n
  }

}

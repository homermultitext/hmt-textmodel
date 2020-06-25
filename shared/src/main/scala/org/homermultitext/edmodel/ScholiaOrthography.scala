package org.homermultitext.edmodel

import edu.holycross.shot.mid.orthography._

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._


/**
*/
object ScholiaOrthography extends MidOrthography {

  def orthography: String = "Orthography of Iliadic scholia"
  
  def tokenCategories: Vector[MidTokenCategory] = Vector.empty[MidTokenCategory]

  def tokenizeNode(n: edu.holycross.shot.ohco2.CitableNode): Vector[MidToken] = Vector.empty[MidToken]

  def validCP(cp: Int): Boolean = false

  def exemplarId : String = "scholtkn"
}

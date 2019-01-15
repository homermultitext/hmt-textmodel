package org.homermultitext.edmodel

import edu.holycross.shot.mid.validator._

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map
import scala.xml._
import scala.io.Source

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._


/**
*/
object IliadOrthography extends MidOrthography {
  def orthography: String = "Orthography of Iliad text"
  def tokenCategories: Vector[MidTokenCategory] = Vector.empty[MidTokenCategory]
  def tokenizeNode(n: edu.holycross.shot.ohco2.CitableNode): Vector[MidToken] = Vector.empty[MidToken]
  def validCP(cp: Int): Boolean = false
}

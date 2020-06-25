package org.homermultitext.edmodel

import edu.holycross.shot.mid.orthography._

import edu.holycross.shot.greek._

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._





import wvlet.log._
/**
*/
object ScholiaOrthography extends MidOrthography with LogSupport {

  // Required by MidOrthography
  def orthography: String = "Orthography of Iliadic scholia"

  // Required by MidOrthography
  def validCP(cp: Int): Boolean = {
    val s = Character.toChars(cp.toInt).toVector.mkString
    if (hiAscii.contains(s)) { true } else {

      // use ASCII conversion from greek package:
      val ascii = literaryAsciiOf(s)
      if (ascii.isEmpty){
        warn("No ASCII encoding found for " + s)
        false

      } else {
        val asciiCP = ascii(0).toInt
        debug(s + " = " + asciiCP)
        validScholiaCP(asciiCP)
      }
    }
  }

  // Required by MidOrthography
  def tokenCategories : Vector[MidTokenCategory] = Vector(
    PunctuationToken, LexicalToken, NumericToken
  )

  // Required by MidOrthography
  def tokenizeNode(n: CitableNode): Vector[MidToken] = {
    val urn = n.urn
    // initial chunking on white space
    val lgs = LiteraryGreekString(n.text)
    val units = lgs.ascii.split(" ").filter(_.nonEmpty)

    val classified = for (unit <- units.zipWithIndex) yield {
      val newPassage = urn.passageComponent + "." + unit._2
      val newVersion = urn.addVersion(urn.versionOption.getOrElse("") + "_tkns")
      val newUrn = CtsUrn(newVersion.dropPassage.toString + newPassage)

      val trimmed = unit._1.trim
      // process praenomina first since "." is part
      // of the token:
      val tokensClassified: Vector[MidToken] = if (trimmed(0) == '"') {
          Vector(MidToken(newUrn, "\"", Some(PunctuationToken)))

      } else {
        val depunctuated = LiteraryGreekString.depunctuate(unit._1)
        val first =  MidToken(newUrn, depunctuated.head, LiteraryGreekString.lexicalCategory(depunctuated.head))

        val trailingPunct = for (punct <- depunctuated.tail zipWithIndex) yield {
          MidToken(CtsUrn(newUrn + "_" + punct._2), punct._1, Some(PunctuationToken))
        }
        first +: trailingPunct
      }
      tokensClassified
    }
    classified.toVector.flatten
  }


  // Required by MidOrthography
  def exemplarId : String = "scholtkn"


  val alphabetString = "*abgdezhqiklmncoprstufxyw'.|()/\\=+,~;.— \n\r⁑"

  val fishtail =  "\u2051"
  val terminalSigma = "ς"
  val sun = "☉"
  val moon = "☾"

  val hiAscii = Set(fishtail, terminalSigma, sun, moon)

  def validScholiaCP(cp: Int): Boolean = {
    val cArray = Character.toChars(cp)
    alphabetString.contains(cArray(0))
  }

}

package org.homermultitext.edmodel

import edu.holycross.shot.mid.orthography._

import edu.holycross.shot.greek._

import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._

//import scala.scalajs.js
//import scala.scalajs.js.annotation._

import wvlet.log._
/**
*/
//@JSExportAll
object ScholiaOrthography extends MidOrthography with LogSupport {
  Logger.setDefaultLogLevel(LogLevel.INFO)
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
    val str = n.text.trim
    val nodeString = if (str.head == '"') {str.tail} else {str}

    val lgs = LiteraryGreekString(nodeString)
    debug(s"FROM ${str} made ${nodeString} and LGS ${lgs.ucode}/${lgs.ascii}")


    val nodeQuoteTokens: Vector[MidToken] = if (str.head == '"') {
      val newPassage = urn.passageComponent + ".q"
      val newVersion = urn.addVersion(urn.versionOption.getOrElse("") + exemplarId)
      val newUrn = CtsUrn(newVersion.dropPassage.toString + newPassage)
      val quoteToken = MidToken(newUrn, "\"", Some(PunctuationToken))

      Vector(quoteToken)
    } else {
      Vector.empty[MidToken]
    }

    // initial chunking is by white space on Unicode representation:
    val units = nodeString.split(" ").filter(_.nonEmpty).toVector.map(_.trim)
    debug("UNITS TO CLASSIFY: " + units)
    // create MidTokens, classified and identified by token-level URNs
    val classified = for ((s, index) <- units.zipWithIndex) yield {
      debug(s"WS token ${index} = ${s}")
      val newPassage = urn.passageComponent + "." + index
      val newVersion = urn.addVersion(urn.versionOption.getOrElse("") + exemplarId)
      val newUrn = CtsUrn(newVersion.dropPassage.toString + newPassage)

      // Catch any leading quotation marks:
      val leadingQuote = if (s.head == '"') {
        Vector(MidToken(newUrn, "\"", Some(PunctuationToken)))
      } else {Vector.empty[MidToken]}

      val tokensClassified: Vector[MidToken] = {
        // split any trailing punctuation into separate tokens
        val depunctuated = depunctuate(s)
        debug("classify " + s + " and depunct " + depunctuated)

        val first =  MidToken(newUrn, depunctuated.head, LiteraryGreekString.lexicalCategory(depunctuated.head))

        val trailingPunct = for ((punct, punctCount) <- depunctuated.tail zipWithIndex) yield {
          MidToken(CtsUrn(newUrn + "_" + punctCount), punct, Some(PunctuationToken))
        }
        leadingQuote ++ (first +: trailingPunct)
      }
      tokensClassified
    }
    nodeQuoteTokens ++ classified.toVector.flatten

  }


  // need to special case fishtail because it's beyond BMP
  // and can't be analyzed as chars
  def depunctuate(s: String): Vector[String] = {

    if (trailingFish(s)) {
      LiteraryGreekString.depunctuate(stripFish(s), Vector(fishtail), punctuationList)
    } else {
      LiteraryGreekString.depunctuate(s.trim, punctuationChars = punctuationList)
    }
  }


  // Required by MidOrthography
  def exemplarId : String = "scholtkn"


  // Things that are hard to type:
  // So protected!
  val quoteChar = '"'
  val q = quoteChar.toString
  val punctuationList = """,~;,"—"""


  /** Outside BMP: need to check it outside of methods using
  * Scala Chars.*/
  val fishtail =  "\u2051"
  // Alphabetic characters outside BMP
  val terminalSigma = "ς"
  val sun = "☉"
  val moon = "☾"

  // "*abgdezhqiklmncoprstufxyw'.|()/\\=+,~;.— \n\r⁑"

  val alphabetString = "*abgdezhqiklmncoprstufxyw'${sun}${moon}\u2051.|()/\\=+" + punctuationList + " \n\r"


  /** RE matching fishtail at end of a string.*/
  val fishRegEx = (fishtail + "$").r
  /** True if s ends in fishtail character.
  *
  * @param s String to test.
  */
  def trailingFish(s: String): Boolean = {
    fishRegEx.findFirstIn(s.trim) match {
      case None => false
      case _ => true
    }
  }

  /** String any trailing fishtail off string.
  *
  * @param s String to strip down.
  */
  def stripFish(s: String): String = {
    fishRegEx.replaceFirstIn(s.trim, "")
  }

  /** Valid code points we cannot treat as Characters,
  * and therefore cannot check with validScholiaCp method.*/
  val hiAscii = Set(fishtail, terminalSigma, sun, moon)

  /** True if a code point is valid.
  *
  * @param cp Codepoint to check.
  */
  def validScholiaCP(cp: Int): Boolean = {
    val cArray = Character.toChars(cp)
    val valid = alphabetString.contains(cArray(0))
    if (!valid){ warn (s"Codepoint ${cp} is not valid.")}
    valid
  }

}

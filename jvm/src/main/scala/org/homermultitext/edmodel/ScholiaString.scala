package org.homermultitext.edmodel

import edu.holycross.shot.greek._
import edu.holycross.shot.mid.orthography._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import edu.holycross.shot.citevalidator._
import edu.holycross.shot.scm._
import edu.holycross.shot.dse._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


import scala.scalajs.js
import scala.scalajs.js.annotation._
import scala.annotation.tailrec

/** Representation of a Greek string written in conventional literary orthography.
*
* @param str A string in either the ascii or ucode representation of the [[ScholiaString]]
* system.
*/
@JSExportAll case class ScholiaString(str: String)  extends LGSTrait with GreekString with Ordered[GreekString] with LogSupport  {
  //Logger.setDefaultLogLevel(LogLevel.INFO)

  require(str.nonEmpty, "Cannot create ScholiaString from empty String")

  //////////////////////////////////////////////
  /////////////// REQUIRED BY LGSTrait
  def ascii = asciiOf(str, ScholiaOrthography.passThrough)
  def ucode = ucodeOf(str, ScholiaOrthography.cpList, ScholiaOrthography.combining, ScholiaOrthography.passThrough)

  /** All valid characters in the ASCII representation of this system
  * in their alphabetic order in Greek.
  */
  def alphabetString =  ScholiaOrthography.alphabetString  //"*abgdezhqiklmncoprstufxyw'.|()/\\=+,:;.— \n\r"

  def punctuationString: String = ScholiaOrthography.punctuationString

  /** Alphabetically ordered Vector of vowel characters in `ascii` view.*/
  def vowels = ScholiaOrthography.vowels //Vector('a','e','h','i','o','u','w')

  /** Alphabetically ordered Vector of consonant characters in `ascii` view.*/
  def consonants = ScholiaOrthography.consonants // ('b','g','d','z','q','k','l','m','n','c','p',
  //'r','s','t','f','x','y') //,'Σ')

  def accents =  ScholiaOrthography.accents

  /** Breathing characters. */
  def breathings = ScholiaOrthography.breathings // Vector(')', '(')
  /** Accent characters. */
  Vector('=', '/', '\\')

  /** */
  def comboChars = ScholiaOrthography.comboChars //Vector('|','+')

  // use concrete encoding in LGSTrait
  def combos: String = fixedCombos(str)


  /////////////// REQUIRED BY GreekString trait
  //
  def stripAccent: ScholiaString = ScholiaString(stripAccentString)

  def stripBreathing: ScholiaString = ScholiaString(stripBreathingString)

  def stripBreathingAccent: ScholiaString = ScholiaString(stripBreathingAccentString)

  def flipGrave: ScholiaString = ScholiaString(flipGraveString)

  def toLower: ScholiaString = ScholiaString(lowerCase)

  def toUpper: ScholiaString = ScholiaString(upperCase)


  //// OTHER FORMATTING METHODS
  def capitalize: ScholiaString = ScholiaString(capitalizeString)

  def depunctuate: ScholiaString = ScholiaString(ScholiaOrthography.depunctuate(ascii).mkString(" "))

}

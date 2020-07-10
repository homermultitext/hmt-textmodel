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
* @param str A string in either the ascii or ucode representation of the [[IliadString]]
* system.
*/
case class IliadString(str: String)  extends LGSTrait with GreekString with Ordered[GreekString] with LogSupport  {
  //Logger.setDefaultLogLevel(LogLevel.INFO)

  require(str.nonEmpty, "Cannot create IliadString from empty String")

  //////////////////////////////////////////////
  /////////////// REQUIRED BY LGSTrait
  def ascii = asciiOf(str, IliadOrthography.passThrough)
  def ucode = ucodeOf(str, IliadOrthography.cpList, IliadOrthography.combining, IliadOrthography.passThrough)

  /** All valid characters in the ASCII representation of this system
  * in their alphabetic order in Greek.
  */
  def alphabetString =  IliadOrthography.alphabetString  //"*abgdezhqiklmncoprstufxyw'.|()/\\=+,:;.— \n\r"

  def punctuationString: String = IliadOrthography.punctuationString

  /** Alphabetically ordered Vector of vowel characters in `ascii` view.*/
  def vowels = IliadOrthography.vowels //Vector('a','e','h','i','o','u','w')

  /** Alphabetically ordered Vector of consonant characters in `ascii` view.*/
  def consonants = IliadOrthography.consonants // ('b','g','d','z','q','k','l','m','n','c','p',
  //'r','s','t','f','x','y') //,'Σ')

  def accents =  IliadOrthography.accents

  /** Breathing characters. */
  def breathings = IliadOrthography.breathings // Vector(')', '(')
  /** Accent characters. */
  Vector('=', '/', '\\')

  /** */
  def comboChars = IliadOrthography.comboChars //Vector('|','+')

  // use concrete encoding in LGSTrait
  def combos: String = fixedCombos(str)


  /////////////// REQUIRED BY GreekString trait
  //
  def stripAccent: IliadString = IliadString(stripAccentString)

  def stripBreathing: IliadString = IliadString(stripBreathingString)

  def stripBreathingAccent: IliadString = IliadString(stripBreathingAccentString)

  def flipGrave: IliadString = IliadString(flipGraveString)

  def toLower: IliadString = IliadString(lowerCase)

  def toUpper: IliadString = IliadString(upperCase)


  //// OTHER FORMATTING METHODS
  def capitalize: IliadString = IliadString(capitalizeString)

  def depunctuate: IliadString = IliadString(IliadOrthography.depunctuate(ascii).mkString(" "))

}

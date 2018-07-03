package org.homermultitext.edmodel




/** Definitions of allowed characters in HMT editions.
*
*
*/
object HmtChars {

  /** Map of modern Greek vowels + tonos to ancient Greek
  * vowel + acute, plus elision mark to plain single quote.
  */
  val greekCpMap: Map[Int, Char] = Map (
    // tonos:
    '\u03ac'.toInt -> '\u1f71', // alpha
    '\u03ad'.toInt -> '\u1f73',//epsilon
    '\u03cc'.toInt -> '\u1f79', // omicron
    '\u03cd'.toInt -> '\u1f7b', // upsilon
    '\u03ce'.toInt -> '\u1f7d', // omega
    // elision
    '\u1fbd'.toInt -> '\u0027'
  )

  // Names for referring to awkward characters:
  val elision = '\u0027'
  val fishtail = '\u2051'
  val cross = '\u2021'
  val backslash = '\\'

  /** Punctuation characters. */
  val punctCPs:  Vector[Char] = Vector('\u003a', '\u003b', '\u002c' , '\u002e', elision, fishtail, cross, '-')

  /** True if character is a legal punctuation character.
  *
  * @param ch Character to check.
  */
  def isPunctuation(ch: Character): Boolean = {
    punctCPs.contains(ch)
  }

  /** True if codepoint is a legal punctuation character.
  *
  * @param ch Codepoint to check.
  */
  def isPunctuation(cp: Int): Boolean = {
    punctCPs.contains(cp)
  }



  /** Quantity markers.  */
  val quants:  Vector[Char] = Vector(
    '_', // macron
    '^' // breve
  )

  /** True if character is a legal quanitity marker.
  *
  * @param ch Character to check.
  */
  def isQuantity(ch: Character): Boolean = {
    quants.contains(ch)
  }
  /** True if codepoint is a legal quanitity marker.
  *
  * @param cp Codepoint to check.
  */
  def isQuantity(cp: Int): Boolean = {
    quants.contains(cp)
  }

  /** ASCII representations of additional "floating" characters.
  */
  val floatChars:  Vector[Char] = Vector(
    backslash, // grave
    '+',  // diaeresis
    '/',  // acute
    '_', // macron
    '^' // breve
  )

  /** True if character is a legal representation of a "floating" character.
  *
  * @param ch Character to check.
  */
  def isFloating(ch: Character): Boolean = {
    floatChars.contains(ch)
  }

  /** True if character is a legal representation of a "floating" character.
  *
  * @param cp Codepoint to check.
  */
  def isFloating(cp: Int): Boolean = {
    floatChars.contains(cp)
  }


  // Remember that Vector.range() yields values up to but not including
  // second param, e.g., Vector.range(1,3) == Vector(1,2)
  /** Codepoints for basic alphabetic characters. */
  val basicAlphabetCPs:  Vector[Char] = Vector.range('\u0391', '\u03a2') ++   Vector.range('\u03a3', '\u03aa') ++  Vector.range('\u0381', '\u03ca') ++ Vector('\u03ca', '\u03cb')

  /** Codepoints in thbe extended Greek range omitting undefined codepoints.*/
  val combinedFormCPs:  Vector[Char] =   Vector.range('\u1f00','\u1f16') ++  Vector.range('\u1f18','\u1f1e') ++ Vector.range('\u1f20', '\u1f46') ++ Vector.range('\u1f48', '\u1f4e') ++ Vector.range('\u1f50', '\u1f58') ++ Vector('\u1f59', '\u1f5b', '\u1f5d') ++ Vector.range('\u1f5f', '\u1f7e') ++ Vector.range('\u1f80', '\u1fb5') ++ Vector.range('\u1fb6','\u1fbd') ++ Vector.range('\u1fc2', '\u1fc5') ++ Vector.range('\u1fc6','\u1fcd') ++ Vector.range('\u1fd0', '\u1fd4') ++ Vector.range('\u1fd6', '\u1fdc') ++ Vector.range('\u1fe0','\u1fed') ++ Vector.range('\u1ff2','\u1ff5') ++ Vector.range('\u1ff6','\u1ffd')

  /** All allowed codepoints. */
  val allowedCPs:  Vector[Char] = basicAlphabetCPs ++  punctCPs ++  combinedFormCPs ++ floatChars


  /** True if codepoint is allowed in HMT editions.
  *
  * @param cp Codepoint to check.
  */
  def legal(cp: Int): Boolean = {
    allowedCPs.contains(cp)
  }

  /** True if character is allowed in HMT editions.
  *
  * @param ch Character to check.
  */
  def legal(ch: Char): Boolean = {
    allowedCPs.contains(ch)
  }

  /** Normalize a set of codepoints allowed in HMT input
  * to specified output form.
  *
  * @param cps Codepoints to normalize.
  */
  def normalizeCPs(cps: Vector[Int]) : Vector[Int] = {
    cps.map( cp => {
      if (greekCpMap.keySet.contains(cp)) {
        greekCpMap(cp)
      } else {
        cp
      }
    })
  }



	/** Recursively compute list of codepoints for a string.
  *
  * @param s String to analyze.
  * @param cpVector Vector previously computed code points.
  * @param idx Index of pointer into s.
   */
	def stringToCps(s: String, cpVector: Vector[Int] = Vector.empty[Int], idx : Int = 0) : Vector[Int] = {
		if (idx >= s.length) {
			cpVector
		} else {
			val cp = s.codePointAt(idx)
			stringToCps(s, cpVector :+ cp, idx + java.lang.Character.charCount(cp))
		}
	}

  /** Compose a String from an array of codepoints.
  *
  * @param cps Vector of codepoints.
  */
  def cpsToString(cps: Vector[Int]): String = {
    val cpArray = cps.toArray
    new String(cpArray,0,cpArray.length)
  }

  def normalizeCPs(s: String) = {

  }
}

package org.homermultitext.edmodel

import edu.holycross.shot.cite._
import edu.holycross.shot.greek._


/** A fully documented, semantically distinct token.
*
* @param sourceUrn URN with subreference for this token in the analyzed text.
* @param editionUrn URN for this token in a token edition with additional
* citation level in the passage hierarchy.
* @param lang 3-letter language code for the language code of this token, or a descriptive string if no ISO code defined for this language.
* @param readings All [[org.homermultitext.edmodel.Reading]]s belonging to this token.
* @param lexicalCategory Lexical category of this token.
* @param lexicalDisambiguation A URN disambiguating named entities.
* @param alternateReading Optional [[org.homermultitext.edmodel.AlternateReading]]s belonging to this token.
* @param discourse Category of discourse of this token.
* @param externalSource Optional URN of a text this token is quoted from.
* @param errors List of error messages (hopefully empty).
*/
case class HmtToken (
  sourceUrn: CtsUrn,
  editionUrn: CtsUrn,
  lang : String = "grc",

  readings: Vector[Reading],

  lexicalCategory: LexicalCategory,
  lexicalDisambiguation: Cite2Urn = Cite2Urn("urn:cite2:hmt:disambig.v1:lexical"),

  alternateReading: Option[AlternateReading] = None,

  discourse: DiscourseCategory = DirectVoice,
  externalSource: Option[CtsUrn] = None,

  errors: Vector[String] = Vector.empty[String]
) {


  /** Create a copy of this token with a given edition URN.
  *
  * @param newUrn CtsUrn for the new token.
  */
  def adjustEditionUrn(newUrn: CtsUrn) : HmtToken  = {
    HmtToken(
      sourceUrn,
      newUrn,
      lang,
      readings,
      lexicalCategory,
      lexicalDisambiguation,
      alternateReading,
      discourse,
      externalSource,
      errors
    )
  }


  /** Create a new [[HmtToken]] with an additional error
  * message added to the list.
  *
  * @param msg Error message to add.
  */
  def addErrorMessage(msg: String) : HmtToken  = {
    HmtToken(
      sourceUrn,
      editionUrn,
      lang,
      readings,
      lexicalCategory,
      lexicalDisambiguation,
      alternateReading,
      discourse,
      externalSource,
      errors :+ msg
    )
  }

  /** String value separating properties in string representation
  * of the object as a single row
  */
  val  propertySeparator = "\t"

  /** String value separating multiple items within a single property
  * in string representation of the object as a single row
  */
  val listSeparator = "#"



  /** Leiden-style formatting of any
  * alternate reading.
  */
  def altString: String = {
    alternateReading match {
      case None => "None"
      case Some(alt) => alt.leidenize
    }
  }

  /** True if token has an alternate reading of any kind.
  */
  def hasAlternate: Boolean = {
    alternateReading match {
      case None => false
      case _ => true
    }
  }

  /** True if token is abbreviated in diplomatic reading.
  */
  def isAbbreviation: Boolean = {
    alternateReading match {
      case None => false
      case Some(alt) => if (alt.alternateCategory == Restoration) {
        true
      } else {
        false
      }
    }
  }

  /** True if scribe offers a variant reading (multiform) for
  * this token.
  */
  def hasScribalMultiform: Boolean = {
    alternateReading match {
      case None => false
      case Some(alt) => if (alt.alternateCategory == Multiform) {
        true
      } else {
        false
      }
    }
  }


  /** True if scribe offers a correction for this token.
  */
  def hasScribalCorrection: Boolean = {
    alternateReading match {
      case None => false
      case Some(alt) => if (alt.alternateCategory == Correction) {
        true
      } else {
        false
      }
    }
  }



  /** True if diplomatic reading matches a String.
  * Comparison is based on Unicode NFC forms of the strings.
  */
  def diplomaticMatch(s: String, accent: Boolean = true): Boolean = {
    if (accent) {
      //def cf =  Normalizer.normalize(s, Normalizer.Form.NFC)
      readWithDiplomatic.contains(s)
    } else {
      val strippedQuery = LiteraryGreekString(s).stripAccent.ucode
      val strippedSrc = LiteraryGreekString(readWithDiplomatic).stripAccent.ucode
      strippedSrc.contains(strippedQuery)
    }
  }


  /** True if reading with scribal correction matches a string.
  *
  * @param s Text to match.
  * @param accent True if accent should be included in matching.
  */
  def scribalMatch(s: String, accent: Boolean = true): Boolean = {
    if (accent) {
      readWithScribal.contains(s)
    } else {
      val strippedQuery = LiteraryGreekString(s).stripAccent.ucode
      val strippedSrc = LiteraryGreekString(readWithScribal).stripAccent.ucode
      strippedSrc.contains(strippedQuery)
    }
  }

  /** True if reading with alternate reading matches a string.
  *
  * @param s Text to match.
  * @param accent True if accent should be included in matching.
  */
  def alternateMatch(s: String, accent: Boolean = true): Boolean = {
    if (accent) {
      readWithAlternate.contains(s)

    }  else {
      println(s"Given ${s} ... compare to ${readWithAlternate}")


      val stripped = LiteraryGreekString(s).stripAccent.ascii
      println("Stripped query to " + stripped)
      val alt = readWithAlternate
      println("Alt " + alt)
      val altText = LiteraryGreekString(alt)
      println(s"Alt lgs ${altText} / ${altText.ascii} / ${altText.ucode}")
      val altStripped = altText.stripAccent.ascii
      println("Stripped source to " + altStripped )

      println("Query " + stripped + " compares to " + altStripped)
      println("Manualy entered == " + LiteraryGreekString("οὕτως").stripAccent.ascii)
      altStripped.contains(stripped)
    }
  }


  /** True if this node's reading matches a string.
  *
  * @param s Text to match.
  * @param readingType Type of reading to use for comparison.  May be
  * one of diplomatic, scribal or alternate.
  */
  def stringMatch(s : String, readingType: String = "diplomatic" ): Boolean = {
    readingType match {
      case "diplomatic" => diplomaticMatch(s)

      case "scribal" => scribalMatch(s)
      case "alternate" => alternateMatch(s)
      case _ => throw new Exception(s"${readingType} is not a valid value for type of reading to match")
    }
  }

  /**  True if value for lexical disambiguation of this token matches
  * a give URN.
  *
  * @param urn The URN to compare.
  */
  def lexMatch(urn: Cite2Urn) : Boolean = {
    lexicalDisambiguation ~~ urn
  }


  /** Format a string representation as a single line of delimited text
  * usng `propertySeparator` value as the delimiter, and `listSeparator`
  * as a secondary delimiter for lists within a single property.
  */
  def rowString: String = {
    //analysis + propertySeparator +
    sourceUrn + propertySeparator +
    editionUrn + propertySeparator +
    lang + propertySeparator +
    Reading.leidenize(readings) + propertySeparator +
    lexicalCategory + propertySeparator + lexicalDisambiguation + propertySeparator + altString + propertySeparator +
    discourse + propertySeparator +
    externalSource  + propertySeparator +
    errors.mkString(listSeparator) + propertySeparator
  }


  /** Format a string representation as one value per line,
  * either with or without labels.
  *
  * @param withLabels include labels if true
  *
  */
  def columnString(withLabels: Boolean): String = {
    val errorString = errors.zipWithIndex.map {
      case (i,s) => (i + 1) + ". " + s
    }.mkString(" ")

    withLabels match {
      case false => {
        Vector(sourceUrn,editionUrn,lang,readings.toString,lexicalCategory.toString,lexicalDisambiguation.toString,alternateReading.toString,discourse.toString,externalSource,errorString).mkString("\n")
      }

      case true => {
        val stringVals = Vector(sourceUrn,editionUrn,lang,readings.toString,lexicalCategory.toString,lexicalDisambiguation.toString,alternateReading.toString,discourse.toString,externalSource,errorString)

        val labelled = stringVals.zip(HmtToken.paddedLabels)
        labelled.map {
          case (stringVal,label) => label + ": " + stringVal
        }.mkString("\n")
      }
    }

  }

  /** Format a string representation as one value per line,
  * including labels.
  */
  def columnString: String = columnString(true)



  /** Collect alternate reading for this token.
  *  Strings are normalized to Unicode form NFC.
  */
  def readWithAlternate: String = {
    alternateReading match {
      case None => {
        readings.map(_.text).mkString
      }
      case Some(alt) => {
        alt.reading.map(_.text).mkString
      }
    }
  }



  /** Collect scribal text, adding corrections to diplomatic readings.
  */
  def readWithScribal: String = {
    alternateReading match {
      case None => {
        readings.map(_.text).mkString

      }
      case Some(alt) => {
        alt.alternateCategory match {
          case Correction => {
            alt.reading.map(_.text).mkString

          }
          case _ => {
            val readingList = readings.filter(r => (r.status == Clear) || (r.status == Unclear))
            readingList.map(_.text).mkString
          }
        }
      }
    }
  }

  /** Collect clear diplomatic readings for this token.
  */
  def readWithDiplomatic: String = {
    readings.map(_.text).mkString
  }


  def readLegible: String = {
    val dipl = readings.filter(_.status == Clear)
    dipl.map(_.text).mkString
  }

  def leidenDiplomatic: String = {
    readings.map{ rdg =>
      rdg.status match {
        case Clear => rdg.leidenize
        case Unclear => rdg.leidenize
        case _ => ""
      }
    }.mkString
  }

  def leidenFull: String = {
    alternateReading match {
      case None => {
        readings.map(_.leidenize).mkString
      }
      case Some(alt) => {
        alt.alternateCategory match {
          case Restoration =>
          readings.map(_.leidenize).mkString +
           alt.reading.map(_.leidenize).mkString
          case _ =>  readings.map(_.leidenize).mkString
        }
      }
    }
  }


  /**  Format human-readable report of errors encountered reading this token.
  *
  * @param separator String to use as structural delimiter in report.
  */
  def errorReport(separator: String = "\t"): String = {
    editionUrn.toString + separator + readings.map(_.leidenize).mkString + separator + errors.mkString(" ++ ")
  }


  /** Compose a string listing properties
  * in form "k=v".
  */
  def kvString : String = {
    "source URN=" + sourceUrn.toString + "\n" +
    "edition URN=" + editionUrn.toString + "\n" +
    "lang=" +lang  + "\n" +
    "lexical cagtegory=" + lexicalCategory.toString
  }
}

/** Factory for labelling information about tokens.
*/
object HmtToken {

  /** English labels for properties */
   val labels = Vector("Source URN", "Edition URN","Language","Readings","Lexical category", "Disambiguation", "Alternate reading", "Discourse category","External source","Errors")

   /** Width in characters to allocate for widest label */
   val labelWidth = labels.map(_.size).max

   /** Pad labels to uniform width in characters */
   def paddedLabels  =  labels.map {
     s => " " * (labelWidth - s.size) + s
   }

}

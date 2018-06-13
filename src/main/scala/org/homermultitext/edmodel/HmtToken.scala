package org.homermultitext.edmodel

import edu.holycross.shot.cite._
import edu.holycross.shot.greek._

import scala.collection.mutable.ArrayBuffer
import java.text.Normalizer

/** A fully documented, semantically distinct token.
* The model of this token supports the ORCA model of
* aligned text analysis.  The `analysis` member is a CITE2
* URN representing this token as an ORCA analysis. The `sourceUrn`
* member is a CTS URN with subreference index identifying
* the specific string of text analyzed.  The`editionUrn`
* member is a CTS URN for this token in an analytical exemplar.
* The other members of the [[HmtToken]] provide the analytical
* data for this token.
*
* @constructor create a token
* @param analysis CITE URN for this token analysis.
* @param sourceUrn URN for this token in the analyzed text
* @param editionUrn URN for this token in an analytical exemplar when promoted to an edition
* @param lang 3-letter language code for the language code of this token, or a descriptive string if no ISO code defined for this language
* @param readings All [[org.homermultitext.edmodel.Reading]]s belonging to this token
* @param lexicalCategory lexical category of this token
* @param lexicalDisambiguation URN for automated method to disambiguate tokens of a given type, or manually disambiguated URN for named entity values
* @param alternateReading optional [[org.homermultitext.edmodel.AlternateReading]]s belonging to this token
* @param discourse category of discourse of this token
* @param externalSource URN of source this token is quoted from
* @param errors list of error messages (hopefully empty)
*/
case class HmtToken (
  var analysis: Cite2Urn,
  var sourceUrn: CtsUrn,
  var editionUrn: CtsUrn,

  var lang : String = "grc",
  var readings: Vector[Reading],
  var lexicalCategory: LexicalCategory,

  var lexicalDisambiguation: Cite2Urn = Cite2Urn("urn:cite2:hmt:disambig.v1:lexical"),
  var alternateReading: Option[AlternateReading] = None,
  var discourse: DiscourseCategory = DirectVoice,
  var externalSource: Option[CtsUrn] = None,

  var errors: ArrayBuffer[String] = ArrayBuffer.empty[String]
) {

  /** String value separating properties in string representation
  * of the object as a single row
  */
  var propertySeparator = "\t"
  /** String value separating multiple items within a single property
  * in string representation of the object as a single row
  */
  var listSeparator = "#"



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

  /** True is token is abbreviated in diplomatic reading.
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
      println("Looking at form C...")
      def cf =  Normalizer.normalize(s, Normalizer.Form.NFC)
      readWithDiplomatic.contains(cf)
    } else {
      val strippedQuery =  Normalizer.normalize(LiteraryGreekString(s).stripAccent.ucode, Normalizer.Form.NFC)
      val strippedSrc = Normalizer.normalize( LiteraryGreekString(readWithDiplomatic).stripAccent.ucode, Normalizer.Form.NFC)
      strippedSrc.contains(strippedQuery)
    }
  }
  def scribalMatch(s: String, accent: Boolean = true): Boolean = {
    if (accent) {
      def cf =  Normalizer.normalize(s, Normalizer.Form.NFC)
      readWithScribal.contains(cf)
    } else {
      val strippedQuery =  Normalizer.normalize(LiteraryGreekString(s).stripAccent.ucode, Normalizer.Form.NFC)
      val strippedSrc = Normalizer.normalize( LiteraryGreekString(readWithScribal).stripAccent.ucode, Normalizer.Form.NFC)
      strippedSrc.contains(strippedQuery)
    }
  }
  def alternateMatch(s: String, accent: Boolean = true): Boolean = {
    if (accent) {
      def cf =  Normalizer.normalize(s, Normalizer.Form.NFC)
      readWithAlternate.contains(cf)

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
    analysis + propertySeparator +
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
        Vector(analysis,sourceUrn,editionUrn,lang,readings.toString,lexicalCategory.toString,lexicalDisambiguation.toString,alternateReading.toString,discourse.toString,externalSource,errorString).mkString("\n")
      }

      case true => {
        val stringVals = Vector(analysis,sourceUrn,editionUrn,lang,readings.toString,lexicalCategory.toString,lexicalDisambiguation.toString,alternateReading.toString,discourse.toString,externalSource,errorString)

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

  def orcaColumn: String = {
    val rows = columnString.split("\n")
    val urns = rows.slice(0,3).toVector.mkString("\n")
    val props = rows.slice(3,10).toVector.mkString("\n")
    "ORCA identifiers:\n" + urns + "\n\n" + "Analysis properties:\n" + props + "\n\n" + "Data quality:\n" + rows(10)
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


  /** Collect alternate reading for this token.
  *  Strings are normalized to Unicode form NFC.
  */
  def readWithAlternate: String = {
    alternateReading match {
      case None => {
        val reading = readings.map(_.reading).mkString
        Normalizer.normalize(reading, Normalizer.Form.NFC)

      }
      case Some(alt) => {
        val reading = alt.reading.map(_.reading).mkString
        Normalizer.normalize(reading, Normalizer.Form.NFC)
      }
    }
  }



  /** Collect scribal text, adding corrections to diplomatic readings.
  */
  def readWithScribal: String = {
    alternateReading match {
      case None => {
        val reading = readings.map(_.reading).mkString
        Normalizer.normalize(reading, Normalizer.Form.NFC)
      }
      case Some(alt) => {
        alt.alternateCategory match {
          case Correction => {
            val reading = alt.reading.map(_.reading).mkString
            Normalizer.normalize(reading, Normalizer.Form.NFC)
          }
          case _ => {
            val readingList = readings.filter(r => (r.status == Clear) || (r.status == Unclear))
            val reading = readingList.map(_.reading).mkString
            Normalizer.normalize(reading, Normalizer.Form.NFC)
          }
        }
      }
    }
  }

  /** Collect clear diplomatic readings for this token.
  */
  def readWithDiplomatic: String = {
    val dipl = readings.filter(_.status == Clear)
    val reading = dipl.map(_.reading).mkString
    Normalizer.normalize(reading, Normalizer.Form.NFC)
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


  def debug : String = {
    "analysis=" + analysis.toString + "\n"    +
    "source URN=" + sourceUrn.toString + "\n" +
    "edition URN=" + editionUrn.toString + "\n" +
    "lang=" +lang  + "\n" +
    "lexical cagtegory=" + lexicalCategory.toString

/*
    nalysis
CITE URN for this token analysis.
sourceUrn
URN for this token in the analyzed text
editionUrn
URN for this token in an analytical exemplar when promoted to an edition
lang
3-letter language code for the language code of this token, or a descriptive string if no ISO code defined for this language
readings
All org.homermultitext.edmodel.Readings belonging to this token
lexicalCategory
lexical category of this token
lexicalDisambiguation
URN for automated method to disambiguate tokens of a given type, or manually disambiguated URN for named entity values
alternateReading
optional org.homermultitext.edmodel.AlternateReadings belonging to this token
discourse
category of discourse of this token
externalSource
URN of source this token is quoted from
errors
list of error messages (hopefully empty)*/
  }
}

/** Factory for labelling information about tokens.
*/
object HmtToken {

  /** English labels for properties */
   val labels = Vector("Analysis URN","Source URN", "Edition URN","Language","Readings","Lexical category", "Disambiguation", "Alternate reading", "Discourse category","External source","Errors")

   /** Width in characters to allocate for widest label */
   val labelWidth = labels.map(_.size).max

   /** Pad labels to uniform width in characters */
   def paddedLabels  =  labels.map {
     s => " " * (labelWidth - s.size) + s
   }

}

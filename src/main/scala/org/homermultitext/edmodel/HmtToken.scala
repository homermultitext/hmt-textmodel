package org.homermultitext.edmodel

import edu.holycross.shot.cite._

import scala.collection.mutable.ArrayBuffer


/** A fully documented, semantically distinct token.
*
* @constructor create a token
* @param analysis CITE URN for this token analysis
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
case class HmtToken ( var analysis: Cite2Urn,
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
  def leidenFull: String = {
"hmm"
  }
  def leidenNormalized: String = {
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


  def errorReport(separator: String = "\t"): String = {
    editionUrn.toString + separator + readings.map(_.leidenize).mkString + separator + errors.mkString(" ++ ")
  }
}

/** Labelling information
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

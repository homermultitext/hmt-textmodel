package org.homermultitext.edmodel

import edu.holycross.shot.cite._

import scala.collection.mutable.ArrayBuffer


/** All possible lexical categories for a token
* are enumerated by case objects extending this trait
*
* The `name` member must be implemented with an English description of the lexical category
*
* Used by [[org.homermultitext.edmodel.HmtToken]] and therefore also by [[org.homermultitext.edmodel.TeiReader]]
*/
sealed trait LexicalCategory {def name : String}
/** parseable lexical token */
case object LexicalToken extends LexicalCategory {val name = "lexical token"}
/** token in Milesian numeric notation */
case object NumericToken extends LexicalCategory {val name = "numeric token"}
/** single punctuation character */
case object Punctuation extends LexicalCategory {val name = "punctuation"}
/** quoted literal string not parseable as a lexical token */
case object LiteralToken extends LexicalCategory {val name = "string literal"}
/** token not parseable due to error in HMT edition */
case object Unintelligible extends LexicalCategory {val name = "unparseable lexical token"}


/** All possible categories for discourse of a token
* are enumerated by case objects extending this trait
*
* The `name` member must be implemented with an English description of the discourse status
*
* Used by [[org.homermultitext.edmodel.HmtToken]] and therefore also by [[org.homermultitext.edmodel.TeiReader]]
*/
sealed trait DiscourseCategory {def name : String}
/** token in direct voice of text */
case object DirectVoice extends DiscourseCategory {val name = "voice of text"}
/** quoted word in the natural language of text */
case object QuotedLanguage extends DiscourseCategory {val
 name = "quoted language"}
 /** quoted string of characters not forming a valid lexical entity */
case object QuotedLiteral extends DiscourseCategory {val name = "quoted literal string"}
/** token in quotation of another text */
case object QuotedText extends DiscourseCategory {val name
 = "quoted passage of text"}


/** A fully documented, semantically distinct token.
*
* @constructor create a token
* @param urn URN for this token in an analytical exemplar
* @param lang 3-letter language code for the language code of this token, or a descriptive string if no ISO code defined for this language
* @param readings All [[org.homermultitext.edmodel.Reading]]s belonging to this token
* @param sourceUrn URN for this token in analyzed text ( requiring subref on the )
* @param analysis CITE URN for the ORCA analysis given by the members of the token
* @param lexicalCategory lexical category of this token
* @param lexicalDisambiguation URN for automated method to disambiguate tokens of a given type, or manually disambiguated URN for named entity values
* @param alternateReading optional [[org.homermultitext.edmodel.AlternateReading]]s belonging to this token
* @param discourse category of discourse of this token
* @param externalSource URN of source this token is quoted from
* @param errors list of error messages (hopefully empty)
*/
case class HmtToken (var urn: CtsUrn,
  var lang : String = "grc",
  var readings: Vector[Reading],
  var sourceUrn: CtsUrn,
  var analysis: CiteUrn,
  var lexicalCategory: LexicalCategory ,
  var lexicalDisambiguation: CiteUrn = CiteUrn("urn:cite:hmt:disambig.lexical.v1"),
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

  /** Format a string representation as a single line of delimited text
  * usng `propertySeparator` value as the delimiter, and `listSeparator`
  * as a secondary delimiter for lists within a single property.
  */
  def rowString: String = {
    urn + propertySeparator +
    sourceUrn + propertySeparator +
    analysis + propertySeparator +
    lang + propertySeparator +
    readings + propertySeparator +
    lexicalCategory + propertySeparator + lexicalDisambiguation + propertySeparator + alternateReading + propertySeparator +
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
        Vector(urn,sourceUrn,analysis,lang,readings.toString,lexicalCategory.toString,lexicalDisambiguation.toString,alternateReading.toString,discourse.toString,externalSource,errorString).mkString("\n")
      }

      case true => {
        val stringVals = Vector(urn,sourceUrn,analysis,lang,readings.toString,lexicalCategory.toString,lexicalDisambiguation.toString,alternateReading.toString,discourse.toString,externalSource,errorString)

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
  def columnString(): String = columnString(true)
}

/** Labelling information
*/
object HmtToken {

  /** English labels for properties */
   val labels = Vector("URN","Source URN", "Analysis","Language","Readings","Lexical category", "Disambiguation", "Alternate reading", "Discourse category","External source","Errors")

   /** Width in characters to allocate for widest label */
   val labelWidth = labels.map(_.size).max

   /** Pad labels to uniform width in characters */
   def paddedLabels  =  labels.map {
     s => " " * (labelWidth - s.size) + s
   }

}

package org.homermultitext.edmodel

import edu.holycross.shot.cite._

import scala.collection.mutable.ArrayBuffer


/** All possible lexical categories for a token
* are enumerated by case objects extending this trait
*
* Used by [[org.homermultitext.edmodel.HmtToken]] and therefore also by [[org.homermultitext.edmodel.TeiReader]]
*/
sealed trait LexicalCategory {def name : String}
case object LexicalToken extends LexicalCategory {val name = "lexical token"}
case object NumericToken extends LexicalCategory {val name = "numeric token"}
case object Punctuation extends LexicalCategory {val name = "punctuation"}
case object LiteralToken extends LexicalCategory {val name = "string literal"}
case object Unintelligible extends LexicalCategory {val name = "unparseable lexical token"}


/** All possible categories for discourse of a token
* are enumerated by case objects extending this trait
*
* Used by [[org.homermultitext.edmodel.HmtToken]] and therefore also by [[org.homermultitext.edmodel.TeiReader]]
*/
sealed trait DiscourseCategory {def name : String}
case object DirectVoice extends DiscourseCategory {val name = "voice of text"}
case object QuotedLanguage extends DiscourseCategory {val
 name = "quoted language"}
case object QuotedLiteral extends DiscourseCategory {val name = "quoted literal string"}
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
* @param lexicalDisambiguation automated method to disambiguate tokens of a given type, or editorial disambiguation of named entity values
* @param alternateReading option if done right...
*
*
* @param errors list of error messages (hopefully empty)
*/
case class HmtToken (var urn: CtsUrn,
  var lang : String = "grc",
  var readings: Vector[Reading],
  var sourceUrn: CtsUrn,

  var analysis: CiteUrn,
  var lexicalCategory: LexicalCategory ,
  var lexicalDisambiguation: CiteUrn = CiteUrn("urn:cite:hmt:disambig.lexical.v1"),

  var alternateReading: AlternateReading = HmtToken.defaultAlternate,

  var discourse: DiscourseCategory = DirectVoice,
  var externalSource: Option[CtsUrn] = None,

  var errors: ArrayBuffer[String] = ArrayBuffer.empty[String]
) {

  var propertySeparator = "\t"
  var listSeparator = "#"

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
  def columnString(): String = columnString(true)
}

object HmtToken {
  val defaultAlternate = AlternateReading(Original, Vector.
   empty[Reading])

   val labels = Vector("URN","Source URN", "Analysis","Language","Readings","Lexical category", "Disambiguation", "Alternate reading", "Discourse category","External source","Errors")

   val labelWidth = labels.map(_.size).max
   def paddedLabels  =  labels.map {
     s => " " * (labelWidth - s.size) + s
   }

   val collectionId = "urn:cite:hmt:urtoken"
   val versionId = "v1"
}

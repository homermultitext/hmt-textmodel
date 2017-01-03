package org.homermultitext.edmodel

import scala.collection.mutable.ArrayBuffer

sealed trait LexicalCategory {def name : String}
case object LexicalToken extends LexicalCategory {val name = "lexical token"}
case object NumericToken extends LexicalCategory {val name = "numeric token"}
case object Punctuation extends LexicalCategory {val name = "punctuation"}
case object LiteralToken extends LexicalCategory {val name = "string literal"}
case object Unintelligible extends LexicalCategory {val name = "unparseable lexical token"}


sealed trait DiscourseCategory {def name : String}
case object DirectVoice extends DiscourseCategory {val name = "voice of text"}
case object QuotedLanguage extends DiscourseCategory {val
 name = "quoted language"}
case object QuotedLiteral extends DiscourseCategory {val name = "quoted literal string"}
case object QuotedText extends DiscourseCategory {val name
 = "quoted passage of text"}

case class HmtToken (var urn: String,
  var lang : String = "grc",
  var readings: Vector[Reading],
  var sourceSubref: String,

  var analysis: String,
  var lexicalCategory: LexicalCategory ,
  var lexicalDisambiguation: String = "Automated disambiguation",

  var alternateReading: AlternateReading = HmtToken.defaultAlternate,

  var discourse: DiscourseCategory = DirectVoice,
  var externalSource: String = "none",

  var errors: ArrayBuffer[String] = ArrayBuffer.empty[String]
) {

  var propertySeparator = "\t"
  var listSeparator = "#"

  def rowString: String = {
    urn + propertySeparator +
    sourceSubref + propertySeparator +
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
        Vector(urn,sourceSubref,analysis,lang,readings.toString,lexicalCategory.toString,lexicalDisambiguation.toString,alternateReading.toString,discourse.toString,externalSource,errorString).mkString("\n")
      }

      case true => {
        val stringVals = Vector(urn,sourceSubref,analysis,lang,readings.toString,lexicalCategory.toString,lexicalDisambiguation.toString,alternateReading.toString,discourse.toString,externalSource,errorString)

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

}

package org.homermultitext.edmodel

import scala.collection.mutable.ArrayBuffer

sealed trait LexicalCategory {def name : String}
case object LexicalToken extends LexicalCategory {val name = "lexical token"}
case object NumericToken extends LexicalCategory {val name = "numeric token"}
case object Punctuation extends LexicalCategory {val name = "punctuation"}
case object LiteralToken extends LexicalCategory {val name = "string literal"}


sealed trait DiscourseCategory {def name : String}
case object DirectVoice extends DiscourseCategory {val name = "voice of text"}
case object QuotedLanguage extends DiscourseCategory {val
 name = "quoted language"}
case object QuotedLiteral extends DiscourseCategory {val name = "quoted literal string"}
case object CitedText extends DiscourseCategory {val name
 = "cited passage of text"}



case class HmtToken (var urn: String,
  var lang : String = "grc",
  var readings: Vector[Reading],
  var lexicalCategory: LexicalCategory ,

  var lexicalDisambiguation: String = "Automated disambiguation",

  var alternateReading: AlternateReading = HmtToken.defaultAlternate,
  var discourse: DiscourseCategory = DirectVoice,
  var errors: ArrayBuffer[String] = ArrayBuffer.empty[String]
) {

  var propertySeparator = "\t"
  var listSeparator = "#"

  def rowString: String = {
    urn + propertySeparator +
    lang + propertySeparator +
    readings + propertySeparator +
    lexicalCategory + propertySeparator + lexicalDisambiguation + propertySeparator + alternateReading + propertySeparator +
    discourse + propertySeparator + errors.mkString(listSeparator) + propertySeparator
  }

  def columnString(withLabels: Boolean): String = {
    val errorString = errors.zipWithIndex.map {
      case (i,s) => (i + 1) + ". " + s
    }.mkString(" ")

    withLabels match {
      case false => {
        Vector(urn,lang,readings.toString,lexicalCategory.toString,lexicalDisambiguation.toString,alternateReading.toString,discourse.toString,errorString).mkString("\n")
      }

      case true => {
        val stringVals = Vector(urn,lang,readings.toString,lexicalCategory.toString,lexicalDisambiguation.toString,alternateReading.toString,discourse.toString,errorString)

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

   val labels = Vector("URN","Language","Readings","Lexical category", "Disambiguation", "Alternate reading", "Discourse category","Errors")

   val labelWidth = labels.map(_.size).max
   def paddedLabels  =  labels.map {
     s => " " * (labelWidth - s.size) + s
   }

}

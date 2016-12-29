package org.homermultitext.edmodel


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
  var discourse: DiscourseCategory = DirectVoice
) {

  def columnString(withLabels: Boolean): String = {
    val stringVals = Vector(lang,readings.toString,lexicalCategory.toString,lexicalDisambiguation.toString,alternateReading.toString,discourse.toString)

    val labelled = stringVals.zip(HmtToken.paddedLabels)
    labelled.map {
      case (label,stringVal) => label + ": " + stringVal
    }.mkString("\n")
    //HmtToken.paddedLabels.mkString("\n")
  }
  def columnString(): String = columnString(true)
}

object HmtToken {
  val defaultAlternate = AlternateReading(Original, Vector.
   empty[Reading])

   val labels = Vector("Language","Readings","Lexical category", "Disambiguation", "Alternate reading", "Discourse category")

   val labelWidth = labels.map(_.size).max
   def paddedLabels  =  labels.map {
     s => " " * (labelWidth - s.size) + s
   }

}

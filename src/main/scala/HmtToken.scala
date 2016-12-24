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
)

object HmtToken {
  val defaultAlternate = AlternateReading(Original, Vector.
   empty[Reading])
}

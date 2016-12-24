package org.homermultitext.edmodel


sealed trait LexicalCategory {def name : String}
case object LexicalToken extends LexicalCategory {val name = "lexical token"}
case object NumericToken extends LexicalCategory {val name = "numeric token"}
case object Punctuation extends LexicalCategory {val name = "punctuation"}
case object LiteralToken extends LexicalCategory {val name = "string literal"}

case class HmtToken (var urn: String,
  var lang : String = "grc",
  var readings: Vector[Reading],
  var lexicalCategory: LexicalCategory /*,

  var lexicalDisambiguation: String = "Automated disambig\
uation",
  var alternateReading: AlternateReading = defaultAlterna\
te,
  var discourse: DiscourseCategory = DirectVoice*/
)

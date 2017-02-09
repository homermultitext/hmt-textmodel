package org.homermultitext.edmodel


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
 case object Citation extends DiscourseCategory{val name = "explicit citation with title of a text"}

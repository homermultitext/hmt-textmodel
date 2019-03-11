package org.homermultitext.edmodel

import edu.holycross.shot.cite._
import edu.holycross.shot.greek._


/** A fully documented, semantically distinct token.
*
* @param sourceUrn URN with subreference for this token in the analyzed text.
* @param editionUrn URN for this token in a token edition with additional
* citation level in the passage hierarchy.
* @param tokenGroup Group this token belongs to.
* @param lang 3-letter language code for the language code of this token, or a descriptive string if no ISO code defined for this language.
* @param readings All [[org.homermultitext.edmodel.Reading]]s belonging to this token.
* @param lexicalCategory Lexical category of this token.
* @param lexicalDisambiguation A URN disambiguating named entities.
* @param alternateReadingTokens Any [[HmtGroupedToken]]s belonging to this token as alternate readings.
* @param discourse Category of discourse of this token.
* @param externalSource Optional URN of a text this token is quoted from.
* @param errors List of error messages (hopefully empty).
*/
case class HmtGroupedToken (
  sourceUrn: CtsUrn,
  editionUrn: CtsUrn,
  tokenGroup: Cite2Urn,

  lang : String = "grc",

  readings: Vector[Reading],

  lexicalCategory: LexicalCategory,
  lexicalDisambiguation: Cite2Urn = Cite2Urn("urn:cite2:hmt:disambig.v1:lexical"),

  alternateReadingTokens: Vector[HmtGroupedToken] = Vector.empty[HmtGroupedToken],

  discourse: DiscourseCategory = DirectVoice,
  externalSource: Option[CtsUrn] = None,

  errors: Vector[String] = Vector.empty[String]
)

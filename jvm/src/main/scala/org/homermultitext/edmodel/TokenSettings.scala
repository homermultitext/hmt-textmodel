package org.homermultitext.edmodel

import edu.holycross.shot.cite._

case class TokenSettings (
  contextUrn: CtsUrn,
  lexicalCategory: LexicalCategory = HmtLexicalToken,
  status: EditorialStatus = Clear,
  alternateCategory:  Option[AlternateCategory] = None,
  lexicalDisambiguation: Cite2Urn = Cite2Urn("urn:cite2:hmt:disambig.v1:lexical"),

  discourse: DiscourseCategory = DirectVoice,
  externalSource: Option[CtsUrn] = None,
  errors:  Vector[String] = Vector.empty[String],
  lang : String = "grc",
  treeDepth: Int = HmtTeiElements.tiers.size
) {

  def addExternalSource(urn: Option[CtsUrn]) = {
      TokenSettings(
        contextUrn,
        lexicalCategory,
        status,
        alternateCategory,
        lexicalDisambiguation,
        discourse,
        externalSource = urn,
        errors,
        lang,
        treeDepth
      )
  }


    def addDisambiguation(urn: Cite2Urn) = {
        TokenSettings(
          contextUrn,
          lexicalCategory,
          status,
          alternateCategory,
          lexicalDisambiguation = urn,
          discourse,
          externalSource,
          errors,
          lang,
          treeDepth
        )
    }

    def addAlternateCategory(alt: Option[AlternateCategory]) = {
        TokenSettings(
          contextUrn,
          lexicalCategory,
          status,
          alternateCategory = alt,
          lexicalDisambiguation,
          discourse,
          externalSource,
          errors,
          lang,
          treeDepth
        )
    }
    def addCategory(newCat: LexicalCategory) : TokenSettings = {
      TokenSettings(
        contextUrn,
        lexicalCategory = newCat,
        status,
        alternateCategory,
        lexicalDisambiguation,
        discourse,
        externalSource,
        errors,
        lang,
        treeDepth
      )
    }


    def addError(msg: String) : TokenSettings = {
      TokenSettings(
        contextUrn,
        lexicalCategory,
        status,
        alternateCategory,
        lexicalDisambiguation,
        discourse,
        externalSource,
        errors :+ msg,
        lang,
        treeDepth
      )
    }


    def addErrors(msgs: Vector[String]) : TokenSettings = {
      TokenSettings(
        contextUrn,
        lexicalCategory,
        status,
        alternateCategory,
        lexicalDisambiguation,
        discourse,
        externalSource,
        errors ++ msgs,
        lang,
        treeDepth
      )
    }

    def setDepth(depth: Int) : TokenSettings = {
      TokenSettings(
        contextUrn,
        lexicalCategory,
        status,
        alternateCategory,
        lexicalDisambiguation,
        discourse,
        externalSource,
        errors,
        lang,
        treeDepth = depth
      )
    }


    def setStatus(newStatus: EditorialStatus) : TokenSettings = {
      TokenSettings(
        contextUrn,
        lexicalCategory,
        status = newStatus,
        alternateCategory,
        lexicalDisambiguation,
        discourse,
        externalSource,
        errors,
        lang,
        treeDepth
      )
    }


    def setDiscourse(discourseType: DiscourseCategory) : TokenSettings = {
      TokenSettings(
        contextUrn,
        lexicalCategory,
        status,
        alternateCategory,
        lexicalDisambiguation,
        discourse = discourseType,
        externalSource,
        errors,
        lang,
        treeDepth
      )
    }


}

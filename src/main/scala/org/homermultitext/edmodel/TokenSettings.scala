package org.homermultitext.edmodel

import edu.holycross.shot.cite._

case class TokenSettings (
  contextUrn: CtsUrn,
  lexicalCategory: LexicalCategory = LexicalToken,
  status: EditorialStatus = Clear,
  discourse: DiscourseCategory = DirectVoice,
  externalSource: Option[CtsUrn] = None,
  errors:  Vector[String] = Vector.empty[String],
  lang : String = "grc",
  treeDepth: Int = HmtTeiElements.tiers.size
) {

    def addCategory(newCat: LexicalCategory) : TokenSettings = {
      TokenSettings(
        contextUrn,
        newCat,
        status,
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
        discourse,
        externalSource,
        errors,
        lang,
        treeDepth
      )
    }


}

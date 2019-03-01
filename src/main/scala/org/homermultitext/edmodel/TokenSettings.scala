package org.homermultitext.edmodel

import edu.holycross.shot.cite._

case class TokenSettings (
  contextUrn: CtsUrn,
  lexicalCategory: LexicalCategory,
  status: EditorialStatus = Clear,
  discourse: DiscourseCategory = DirectVoice,
  externalSource: Option[CtsUrn] = None,

  lang : String = "grc") {

    def addCategory(newCat: LexicalCategory) : TokenSettings = {
      TokenSettings(
        contextUrn,
        newCat,
        status,
        discourse,
        externalSource,
        lang
      )
    }
}

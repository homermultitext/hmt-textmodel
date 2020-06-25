package org.homermultitext.edmodel
import org.scalatest._


 class LexicalCategorySpec extends FlatSpec {
   "A lexical category" should "have a name" in {
     assert(HmtLexicalToken.name == "lexical token")
   }
}

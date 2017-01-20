package org.homermultitext.edmodel
import org.scalatest._
import org.scalatest.prop.TableDrivenPropertyChecks
 /*

class LexicalCategorySpec extends UnitSpec with TableDrivenPropertyChecks  {
   it should "provide string name for vaue" in pending
   {
      val validCombos = Table(
         (LexicalToken,  "lexical token"),
         (NumericToken,  "numeric token"),
         (LiteralToken,  "string literal"),
         (Punctuation,  "punctuation")
     )

   forAll(validCombos) {
     (lexCat: LexicalCategory, catName:String) => lexCat.name shouldBe catName
   }
 }*/

 class LexicalCategorySpec extends FlatSpec {
   "A lexical category" should "have a name" in {
     assert(LexicalToken.name == "lexical token")
   }
}

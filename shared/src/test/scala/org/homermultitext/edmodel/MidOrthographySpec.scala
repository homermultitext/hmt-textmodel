package org.homermultitext.edmodel
import org.scalatest._

class MidOrthographySpec extends FlatSpec {

   "The library" should "include case objects for all edition types" in {
     assert(HmtNamedEntityEdition.label == "named entities")
     assert (HmtDiplomaticEdition.label ==  "diplomatic edition")
     assert (HmtScribalNormalizedEdition.label ==  "scribal edition")
     assert (HmtEditorsNormalizedEdition.label ==  "editorially normalized edition")
   }


}

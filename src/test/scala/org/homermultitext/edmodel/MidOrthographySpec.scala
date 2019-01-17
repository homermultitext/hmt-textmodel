package org.homermultitext.edmodel
import org.scalatest._

class MidOrthographySpec extends FlatSpec {

   "The libraryt" should "include case objects for all edition types" in {
     assert(HmtNamedEntityEdition.label == "named entities")
   }
}

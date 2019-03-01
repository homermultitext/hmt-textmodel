package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import org.scalatest.FlatSpec


class TokenSettingsSpec extends FlatSpec {


  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")

  "A TokenSettings" should "default to language 'grc'" in {
    val settings = TokenSettings(context, LexicalToken)
    assert (settings.lang == "grc")
  }

  it should "default to discourse category of direct voice" in {
    val settings = TokenSettings(context, LexicalToken)
    assert(settings.discourse == DirectVoice)
  }

  it should "identify the category of lexical token" in {
    val settings = TokenSettings(context, NumericToken)
    assert(settings.lexicalCategory == NumericToken)
  }



}

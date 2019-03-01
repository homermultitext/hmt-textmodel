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

  it should "create a new settings for a specified token category" in {
    val lexSetting = TokenSettings(context, LexicalToken)
    val numSetting = lexSetting.addCategory(NumericToken)

    assert(numSetting.lexicalCategory == NumericToken)
  }

  it should "create a new settings by adding an error message" in {
      val simpleSetting = TokenSettings(context, LexicalToken)
      val message = "Catastrophic error!"
      val withMessage = simpleSetting.addError(message)
      val expectedErrors = 1
      assert(withMessage.errors.size == expectedErrors)
      assert (withMessage.errors(0) == message)
  }



}

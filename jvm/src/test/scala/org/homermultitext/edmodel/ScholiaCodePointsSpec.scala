package org.homermultitext.edmodel

import org.scalatest.FlatSpec



class ScholiaCodePointsSpec extends FlatSpec {

  "The ScholiaOrthography object" should "identify valid code points including fishtail and tilde for high stop" in {
    val scholion = "νῆας τὸν τόπον τῶν νηῶν~  Ἑλλήσποντον δὲ, τὴν μέχρι Σιγείου θάλασσαν⁑"
    val ok = ScholiaOrthography.validString(scholion)
    val errorsHilited = ScholiaOrthography.hiliteBadCps(scholion)
    assert(ok)
    assert(errorsHilited == scholion)
  }

  it should "recognize Unicode sun" in {
    val va_scholion8_2  = "ὅτε δὲ τὸ ἀπο ἀνατολῆς ἕως μεσημβρίας τοῦ ☉  διάστημα ὡς ἐν τῷ ἠῶς δέ μοι ἐστὶν ."

    val ok = ScholiaOrthography.validString(va_scholion8_2)
    val errors = ScholiaOrthography.hiliteBadCps(va_scholion8_2)
    assert(ok)
    assert(errors == va_scholion8_2)

  }

  it should "recognize Unicode moon" in {
    val scholion = "ὡς δ' ὅτ' ἐν οὐρανω ἄστρα φαεινὴν ἀμφὶ  ☾"
    val ok = ScholiaOrthography.validString(scholion)
    val errors = ScholiaOrthography.hiliteBadCps(scholion)
    assert(ok)
    assert(errors == scholion)
  }

  it should "reject evil Unicode Greek high stop" in {
    val evil  = "σημαίνει πολλά·"
    assert(ScholiaOrthography.validString(evil) == false)

    val expected = "σημαίνει πολλά **·**"
    assert(ScholiaOrthography.hiliteBadCps(evil).trim == expected)
  }

}

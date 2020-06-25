package org.homermultitext.edmodel

import org.scalatest.FlatSpec



class ScholiaOrthographySpec extends FlatSpec {

  "The ScholiaOrthography object" should "have a label" in {
    val expected = "Orthography of Iliadic scholia"
    assert(ScholiaOrthography.orthography == expected)
  }

  it should "identify valid code points" in {
    val scholion = "νῆας τὸν τόπον τῶν νηῶν~  Ἑλλήσποντον δὲ, τὴν μέχρι Σιγείου θάλασσαν⁑"
    val ok = ScholiaOrthography.validString(scholion)
    val errorsHilited = ScholiaOrthography.hiliteBadCps(scholion)
    assert(ok)
    assert(errorsHilited == scholion)
    
  }

  it should "recognize Unicode sun" in {
    val va_scholion8_2  = "σημαίνει πολλά· ὁτὲ μὲν τὸ κατάστημα τῆς ἡμέρας ὡς ἔχει ἐνταυθ  ὅτε δὲ καὶ ἡ σωματικὴ θεός ὡς ἔχει ἐπι τοῦ ἠὼς δὲ  ἐκ λεχέων , παραγανοῦ Τιθωνοῖο · ὅτε δὲ τὸ ἀπο ἀνατολῆς ἕως μεσημβρίας τοῦ ☉  διάστημα ὡς ἐν τῷ ἠῶς δέ μοι ἐστὶν . ἡδε δυωδεκάτη ὅτ' εἰς Ἴ̈λιον εἰλήλουθα ⁑"

    val ok = ScholiaOrthography.validString(va_scholion8_2)
    val errors = ScholiaOrthography.hiliteBadCps(va_scholion8_2)
    println (ok + " for "  + va_scholion8_2 + "\n" + errors)
  }

  it should "recognize Unicode moon" in {
    val scholion = "ὡς δ' ὅτ' ἐν οὐρανω ἄστρα φαεινὴν ἀμφὶ  ☾"
    val ok = ScholiaOrthography.validString(scholion)
    val errors = ScholiaOrthography.hiliteBadCps(scholion)
    println (ok + " for "  + scholion + "\n" + errors)
  }

  it should "reject evil Unicode Greek high stop" in {
    val evil  = "σημαίνει πολλά·"
    assert(ScholiaOrthography.validString(evil) == false)

    val expected = "σημαίνει πολλά **·**"
    assert(ScholiaOrthography.hiliteBadCps(evil).trim == expected)
  }

}

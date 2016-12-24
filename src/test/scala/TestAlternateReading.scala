package org.homermultitext.edmodel
import org.scalatest.FlatSpec

class AlternateReadingSpec extends FlatSpec {
  "An alternate reading" should "have a vector of 0 or more readings" in {
    val rdg = Reading("οὕτως",Restored)
    val alt = AlternateReading(Restoration, Vector(rdg))
    assert (alt.reading.size == 1)
  }
  it should "have a category" in {
  val rdg = Reading("οὕτως",Restored)
  val alt = AlternateReading(Restoration, Vector(rdg))
  assert (alt.alternateCategory == Restoration)
  }
  it should "have no readings when its category is Original" in {
    val alt = AlternateReading(Original,Vector[Reading]())
    assert (alt.reading.size == 0)
  }
  it should "throw an exception if readings are given when category Original" in  pending
  it should "have 1 or more readings when its category is not Original" in pending
  it should "throw an exception if no readings are given when category is not Original" in  pending
  it should "require that all readings be Restored when alternate category is Restoration"
}

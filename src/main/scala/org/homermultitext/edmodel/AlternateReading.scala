package org.homermultitext.edmodel


sealed trait AlternateCategory {def name : String}
case object Restoration extends AlternateCategory {val name = "editorial restoration or completion"}
case object Multiform extends AlternateCategory {val name
 = "scribally recorded multiform"}
case object Correction extends AlternateCategory {val name = "scribal correction"}


case class AlternateReading (
  var alternateCategory: AlternateCategory,
  var reading: Vector[Reading]
)
object AlternateReading {
  def alternative (alt: AlternateReading): String = {
    alt.reading.map(rdg => Reading.typedText(rdg)).mkString(" + ")
  }
}

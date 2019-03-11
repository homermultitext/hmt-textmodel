package org.homermultitext.edmodel

import scala.xml._

object HmtTeiChoice {
  // Allowed combinations for TEI choice:
  val abbrExpan = Set("abbr","expan")
  val sicCorr = Set("sic", "corr")
  val origReg = Set("orig", "reg")

  val choicePairs = Vector(abbrExpan, sicCorr, origReg)

  def choiceChildren(choiceElem: scala.xml.Elem): Vector[String] = {
    choiceElem.child.map(_.label).distinct.filterNot(_ == "#PCDATA").toVector
  }

  // check structure of TEI choice
  def validChoice(choiceElem: scala.xml.Elem): Boolean = {
    val cNames = choiceChildren(choiceElem).toSet
    if (choicePairs.filter(_ == cNames ).nonEmpty) {
      true
    } else {
      false
    }
  }



  def pairedToken(t1: Vector[HmtToken], t2: Vector[HmtToken], settings: TokenSettings): Vector[HmtToken] = {
    // we want to unify readings and alt readings
    println("\n==>UNIFY:\n1." + t1)
    println("\n\n2. " + t2)

    // peek at 1 to see if has alt.
    t1.head.alternateReading match {
      case None => {
        // then we have the reading:
        for (tkn <- t1) yield {

        }
      }
      case _ => {

      }
    }
  
    Vector.empty[HmtToken]
  }

}

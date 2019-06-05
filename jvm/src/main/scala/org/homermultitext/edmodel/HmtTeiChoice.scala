package org.homermultitext.edmodel

import scala.xml._


/** Object for parsing content contained in a TEI `choice` element
* into the HMT project object model of an edition. */
object HmtTeiChoice {
  // Allowed combinations for TEI choice:
  val abbrExpan = Set("abbr","expan")
  val sicCorr = Set("sic", "corr")
  val origReg = Set("orig", "reg")

  val choicePairs = Vector(abbrExpan, sicCorr, origReg)

  /** Extact child elements from a choice.
  *
  * @param choiceElem A parsed TEI choice element.
  */
  def choiceChildren(choiceElem: scala.xml.Elem): Vector[String] = {
    choiceElem.child.map(_.label).distinct.filterNot(_ == "#PCDATA").toVector
  }


  /** True if pairing of two child elements within TEI choice
  * is an allowed pairing in HMT project conventions.
  *
  * @param choiceElem A parsed TEI choice element.
  */
  def validChoice(choiceElem: scala.xml.Elem): Boolean = {
    val cNames = choiceChildren(choiceElem).toSet
    if (choicePairs.filter(_ == cNames ).nonEmpty) {
      true
    } else {
      false
    }
  }


  /**
  * Parse a TEI choice element into a Vector of [HmtToken]s.
  *
  *
  */
  def pairedToken(v1: Vector[HmtToken], v2: Vector[HmtToken], settings: TokenSettings): Vector[HmtToken] = {
    // we want to unify readings and alt readings
    //println("\n==>UNIFY:\npaired elem 1." + v1)
    //println("\n\npaired elem 2. " + v2)

    // peek at 1 to see if has alt.
    val paired = v1.head.alternateReading match {
      case None => {
        val alts =for (tkn2 <- v2) yield {
          tkn2.alternateReading.get
        }
        //println("status:  " + alts(0).alternateCategory + " text "+ alts.map(_.readings).flatten + "\n\n")
        val altRdgs = alts.map(_.readings).flatten
        //println("ALT: cat "+ alts(0).alternateCategory + "with readings " +altRdgs.mkString(" -- "))
        val altOpt = Some(AlternateReading(alts(0).alternateCategory, altRdgs))
        // then we have the reading:
        val tknsWithAlt = for (tkn <- v1) yield {
          tkn.addAlternateReading(altOpt)
        }
        //println("GOT TOKENS WITH ALT " + tknsWithAlt.mkString("\n\n"))
        tknsWithAlt
      }
      case _ => {
        println("V2 has the reading")
        val alts =for (tkn2 <- v1) yield {
          tkn2.alternateReading.get
        }
        //println("status:  " + alts(0).alternateCategory + " text "+ alts.map(_.readings).flatten + "\n\n")
        val altRdgs = alts.map(_.readings).flatten
        //println("ALT: cat "+ alts(0).alternateCategory + "with readings " +altRdgs.mkString(" -- "))
        val altOpt = Some(AlternateReading(alts(0).alternateCategory, altRdgs))
        // then we have the reading:
        val tknsWithAlt = for (tkn <- v2) yield {
          tkn.addAlternateReading(altOpt)
        }
        //println("GOT TOKENS WITH ALT " + tknsWithAlt.mkString("\n\n"))
        tknsWithAlt
      }
    }
    paired
  }

}

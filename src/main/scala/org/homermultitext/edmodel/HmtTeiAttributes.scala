package org.homermultitext.edmodel
import scala.xml._
import edu.holycross.shot.cite._

trait AttributeRequirement {
  /** Name of TEI element. */
  def elName : String

  /** True if requirements are satisfied.*/
  def ok(n: scala.xml.Elem): Boolean
}

case object Foreign extends AttributeRequirement {
  def elName = "foreign"

  def ok(el: scala.xml.Elem) = {
    val nAttr = el \ "@lang"
    nAttr.nonEmpty && el.text.nonEmpty
  }
}

case object PersName extends AttributeRequirement {
  def elName = "persName"

  def ok(el: scala.xml.Elem) = {
    val nAttr = el \ "@n"
    try {
      val u = Cite2Urn(nAttr.toString)
      el.text.nonEmpty

    } catch {
      case t : Throwable => {
        println("Something went wrong with URN value " + nAttr.toString + ". " + t)
        false
      }
    }
  }
}

case object PlaceName extends AttributeRequirement {
  def elName = "placeName"
  def ok(el: scala.xml.Elem) = {
    val nAttr = el \ "@n"
    try {
    val u = Cite2Urn(nAttr.toString)
    el.text.nonEmpty
    } catch {
      case t : Throwable => {
        println("Something went wrong with URN value " + nAttr.toString)
        false
      }
    }
  }
}

case object Num extends AttributeRequirement {
  def elName = "num"
  def ok(el: scala.xml.Elem) = {
    val nAttr = el \ "@value"
    // add check with numeric parsing of text contents!
    try {
      nAttr.toString.toInt
      el.text.nonEmpty
    } catch {
      case nfe:  NumberFormatException => {
        println("Could not format number from value attribute: " + nAttr.toString)
        false
      }
      case t : Throwable => {
        println("Something went wrong with numeric value " + nAttr.toString)
        false
      }
    }
  }
}

case object Rs extends AttributeRequirement {
  def elName = "rs"

  def cite2UrnOk(urnStr: String) : Boolean = {
    try {
      val u = Cite2Urn(urnStr)
      true
    } catch {
      case t : Throwable => {
        println("Something wrong with Cite2Urn value " + urnStr + ".  " + t)
        false
      }
    }
  }


  def ok(el: scala.xml.Elem) = {
    (el \ "@type").toString match {

      case "waw" => el.text.nonEmpty

      case "ethnic" => {
        if (cite2UrnOk((el \ "@n").toString)) {
          el.text.nonEmpty
        } else {
          false
        }
      }

      case "astro" => {
        if(cite2UrnOk((el \ "@n").toString)) {
          el.text.nonEmpty
        } else {
          false
        }

      }

      case "" => {
        println("Empty or missing type attribute on rs")
        false
      }

      case s: String => {
        println("Unrecognized type attribute value on rs: " + s)
        false
      }
    }
  }
}





case object Title extends AttributeRequirement {
  def elName = "title"
  def ctsUrnOk(urnStr: String) : Boolean = {
    try {
      val u = CtsUrn(urnStr)
      true
    } catch {
      case t : Throwable => {
        println("Something wrong with CtsUrn value " + urnStr + ".  " + t)
        false
      }
    }
  }
  def ok(el: scala.xml.Elem) = {
    val nAttr = el \ "@n"
    el.attribute("n") match {
      case None => true
      case _ => {
        ctsUrnOk(nAttr.toString) && el.text.nonEmpty
      }
    }
  }

}


object HmtTeiAttributes {
  def required = Set(
    Foreign, PersName, PlaceName, Num, Rs
  )

  def optional = Set(Title)

  def allReqs = required ++ optional

  def requirements(elem: Elem): Option[AttributeRequirement] = {
    val reqs = allReqs.filter(_.elName == elem.label)
    if (reqs.isEmpty) {
      None
    } else {
      Some(reqs.head)
    }
  }

  def attributeRequired(elem: Elem): Boolean = {
    val requiredAtt = required.filter(_.elName == elem.label)
    requiredAtt.nonEmpty
  }

  def attributeOptional(elem: Elem): Boolean = {
    val options = optional.filter(_.elName == elem.label)
    options.nonEmpty
  }

  def ok(elem: Elem): Boolean = {
    val elemReqs = requirements(elem)
    elemReqs match {
      case None => {
        println("No attribute requirements for " + elem.label)
        false
      }

      case _ => elemReqs.get.ok(elem)
    }
  }
}

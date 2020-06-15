package org.homermultitext.edmodel
import scala.xml._
import edu.holycross.shot.cite._


import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter



/** A specification of attribute usage for a single
* TEI element.*/
trait AttributeRequirement extends LogSupport {
  /** Name of TEI element. */
  def elName : String

  /** True if requirements are satisfied.*/
  def ok(n: scala.xml.Elem): Boolean

  /** Optional error message if attribute usage
  * violates HMT conventions.*/
  def errorMsg(n: scala.xml.Elem): Option[String]
}


/** Attribute requirements for TEI 'foreign'. */
case object Foreign extends AttributeRequirement {
  def elName = "foreign"

  def ok(el: scala.xml.Elem) = {
    val nAttr = el \ "@lang"
    nAttr.nonEmpty && el.text.nonEmpty
  }

  def errorMsg(el: scala.xml.Elem) = {
    if (ok(el)) {
      None
    } else {
      Some("For TEI 'foreign', @lang attribute with language code is mandatory.")
    }
  }
}

/** Attribute requirements for TEI 'persName'. */
case object PersName extends AttributeRequirement {
  def elName = "persName"

  def ok(el: scala.xml.Elem) = {
    val nAttr = el \ "@n"
    try {
      val u = Cite2Urn(nAttr.toString)
      el.text.nonEmpty

    } catch {
      case t : Throwable => {
        warn("Something went wrong with URN value " + nAttr.toString + ". " + t)
        false
      }
    }
  }

  def errorMsg(el: scala.xml.Elem) = {
    if (ok(el)) {
      None
    } else {
      Some("For TEI 'persName', @n attribute with URN from persName collection urn:cite2:hmt:pers.v1: is mandatory.")
    }
  }
}


/** Attribute requirements for TEI 'placeName'. */
case object PlaceName extends AttributeRequirement {
  def elName = "placeName"
  def ok(el: scala.xml.Elem) = {
    val nAttr = el \ "@n"
    try {
    val u = Cite2Urn(nAttr.toString)
    el.text.nonEmpty
    } catch {
      case t : Throwable => {
        warn("Something went wrong with URN value " + nAttr.toString)
        false
      }
    }
  }

  def errorMsg(el: scala.xml.Elem) = {
    if (ok(el)) {
      None
    } else {
      Some("For TEI 'place', @n attribute with URN in HMT place name collection urn:cite2:hmt:place.v1: is mandatory.")
    }
  }
}



/** Attribute requirements for TEI 'num'. */
case object Num extends AttributeRequirement {
  def elName = "num"
  def ok(el: scala.xml.Elem) = {
    val nAttr = el \ "@value"
    // text contents must parse numerically: in
    // HMT editions, these will always be Ints.
    try {
      nAttr.toString.toInt
      el.text.nonEmpty
    } catch {
      case nfe:  NumberFormatException => {
        warn("Could not format number from value attribute: " + nAttr.toString)
        false
      }
      case t : Throwable => {
        warn("Something went wrong with numeric value " + nAttr.toString)
        false
      }
    }
  }

  def errorMsg(el: scala.xml.Elem) = {
    if (ok(el)) {
      None
    } else {
      Some("For TEI 'num', @value attribute with numeric value of element's contents is mandatory.")
    }
  }
}

/** Attribute requirements for TEI 'rs'. */
case object Rs extends AttributeRequirement {
  def elName = "rs"

  def cite2UrnOk(urnStr: String) : Boolean = {
    try {
      val u = Cite2Urn(urnStr)
      true
    } catch {
      case t : Throwable => {
        warn("Something wrong with Cite2Urn value " + urnStr + ".  " + t)
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
        warn("Empty or missing type attribute on rs")
        false
      }

      case s: String => {
        warn("Unrecognized type attribute value on rs: " + s)
        false
      }
    }
  }

  def errorMsg(el: scala.xml.Elem) = {
    if (ok(el)) {
      None
    } else {

      (el \ "@type").toString match {
        case "waw" => Some("For TEI 'rs' with type 'waw', text contents must not be empty.")

        case "ethnic" => {
          Some("For TEI 'rs' with type 'ethnic', text contents must not be empty, and @n attribute must be a valid CITE2 URN.")
        }

        case "astro" => {
          Some("For TEI 'rs' with type 'astro', text contents must not be empty, and @n attribute must be a valid CITE2 URN.")
        }

        case "" => {
          Some("For TEI 'rs' element, @type attribute of 'waw', 'ethnic' or 'astro' is required.")
        }

        case s: String => {
          Some("For TEI 'rs' element, @type attribute  must be one of 'waw', 'ethnic' or 'astro'.")
        }
      }
    }
  }
}

/** Attribute requirements for TEI 'title'. */
case object Title extends AttributeRequirement {
  def elName = "title"

  def cite2UrnOk(urnStr: String) : Boolean = {
    try {
      val u = Cite2Urn(urnStr)
      true
    } catch {
      case t : Throwable => {
        warn("Something wrong with Cite2Urn value " + urnStr + ".  " + t)
        false
      }
    }
  }

  def ok(el: scala.xml.Elem) = {
    val nAttr = el \ "@n"
    el.attribute("n") match {
      case None => true
      case _ => {
        cite2UrnOk(nAttr.toString) && el.text.nonEmpty
      }
    }
  }

  def errorMsg(el: scala.xml.Elem) = {
    if (ok(el)) {
      None
    } else {
      Some("For TEI 'title', @n attribute is optional but must be a valid Cite2Urn if included.")
    }
  }
}


/** Singleton object for validating an XML element's
* compliance with HMT project requirements on usage
* of XML attributes.*/
object HmtTeiAttributes  extends LogSupport {

  /** Set of requirements for mandatory attribute usage.*/
  def required = Set(
    Foreign, PersName, PlaceName, Num, Rs
  )

  /** Set of requirements for optional attribute usage.*/
  def optional = Set(Title)

  /** Requirements for all attribute usage in HMT editions.*/
  def allReqs = required ++ optional


  /** For any given XML element, find an option for
  * the corresponding [[AttributeRequirement]].
  *  This willbe None if no attribute requirements apply.
  *
  * @param elem Element to analyze.
  */
  def requirements(elem: Elem): Option[AttributeRequirement] = {
    val reqs = allReqs.filter(_.elName == elem.label)
    if (reqs.isEmpty) {
      None
    } else {
      Some(reqs.head)
    }
  }

  /** True if this element has requirements for mandatory attribute  usage.
  *
  * @param elem Element to analyze.
  */
  def attributeRequired(elem: Elem): Boolean = {
    val requiredAtt = required.filter(_.elName == elem.label)
    requiredAtt.nonEmpty
  }

  /** True if this element has requirements for optional attribute  usage.
  *
  * @param elem Element to analyze.
  */
  def attributeOptional(elem: Elem): Boolean = {
    val options = optional.filter(_.elName == elem.label)
    options.nonEmpty
  }

  /** True if attribute usage meets HMT requirements.
  *
  * @param elem Element to analyze.
  */
  def ok(elem: Elem): Boolean = {
    val elemReqs = requirements(elem)
    elemReqs match {
      case None => {
        info("No attribute requirements for " + elem.label)
        false
      }
      case _ => elemReqs.get.ok(elem)
    }
  }

  /** Optional error message if attribute usage
  * violates HMT conventions.
  * @param elem Element to analyze.
  */
  def errorMsg(elem: Elem): Option[String] = {
    val elemReqs = requirements(elem)
    elemReqs match {
      case None =>  None
      case _ => elemReqs.get.errorMsg(elem)
    }
  }
}

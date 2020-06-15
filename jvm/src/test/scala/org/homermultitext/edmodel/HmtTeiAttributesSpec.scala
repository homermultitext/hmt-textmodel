package org.homermultitext.edmodel
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.xml._

import org.scalatest.FlatSpec

class HmtTeiAttributesSpec extends FlatSpec {


  val context = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:1.1")


  "The HmtTeiAttributes object" should "require attributes on TEI foreign" in  {
    val str = "<foreign n=\"eng\">Homer</foreign>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.attributeRequired(el))
  }
  it should "check attribute requirements on TEI foreign" in {
    val str = "<foreign n=\"eng\">Homer</foreign>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.attributeRequired(el))
  }



  //persName
  it should "require attributes on TEI persName" in  {
    val str = "<persName n=\"urn:cite2:hmt:persName.v1:HomerHimself\">Homer</persName>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.attributeRequired(el))
  }
  it should "check attribute requirements on TEI persName" in  {
    val str = "<persName n=\"urn:cite2:hmt:persName.v1:HomerHimself\">Homer</persName>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.ok(el))
  }
  it should "check attribute URN value of n attribute on TEI persName" in  {
    val str = "<persName n=\"Not a URN\">Homer</persName>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.ok(el) == false)
  }


  //placeName
  it should "require attributes on TEI placeName" in  {
    val str = "<placeName n=\"urn:cite2:hmt:placeName.v1:Greece\">Greek</placeName>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.attributeRequired(el))
  }
  it should "check attribute requirements on TEI placeName" in  {
    val str = "<placeName n=\"urn:cite2:hmt:placeName.v1:Greece\">Greek</placeName>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.ok(el))
  }
  it should "check attribute URN value of n attribute on TEI placeName" in {
    val str = "<placeName n=\"Not a URN\">Greek</placeName>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.ok(el) == false)
  }


  // num
  it should "require attributes on TEI num" in  {
    val str = "<num value=\"1\">α</num>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.attributeRequired(el))
  }
  it should "check attribute requirements on TEI num" in  {
    val str = "<num value=\"1\">α</num>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.ok(el))
  }
  it should "report missing attribute on TEI num" in  {
    val str = "<num>α</num>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.ok(el) == false)
  }
  it should "catch bad values for num" in  {
    val str = "<num value=\"X\">α</num>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.ok(el) == false)
  }


  // rs
  it should "require attributes on TEI rs" in  {
    val str = "<rs type=\"waw\">α</rs>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.attributeRequired(el))
  }
  it should "check attribute requirements on TEI rs with type ethnic" in  {
    val str = "<rs type=\"ethnic\" n=\"urn:cite2:hmt:placeName.v1:myGuys\">los griegos</rs>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.attributeRequired(el))
  }
  it should "check URN on n attribute for TEI rs with type ethnic" in  {
    val str = "<rs type=\"ethnic\" n=\"urn:cite2:hmt:placeName.v1:myGuys\">los griegos</rs>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.ok(el))
  }
  it should "catch bad URN values on n attribute for TEI rs with type ethnic" in  {
    val str = "<rs type=\"ethnic\" n=\"Not a URN\">los griegos</rs>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.ok(el) == false)
  }


  it should "check attribute requirements on TEI rs with type astro" in  {
    val str = "<rs type=\"astro\" n=\"urn:cite2:hmt:astro.v1:TheMOOON\">Mand</rs>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.attributeRequired(el))
  }
  it should "check URN on n attribute for TEI rs with type astro" in  {
    val str = "<rs type=\"astro\" n=\"urn:cite2:hmt:astro.v1:theMOOON\">Mand</rs>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.ok(el))
  }
  it should "catch bad URN values on n attribute for TEI rs with type astro" in  {
    val str = "<rs type=\"astro\" n=\"Not a URN\">Mand</rs>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.ok(el) == false)
  }
  // Optional n, but if present must be legit CtsUrn
  it should "allow TEI title with no attributes" in  {
    val str = "<title>The Lost Books of Hecataeus</title>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.ok(el))
  }
  it should "check URN value on optional n attribute if present on TEI title" in {
    val str = "<title n=\"urn:cite2:hmt:citedworks.v1:work1\">Odyssey</title>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.ok(el))
  }
  it should  "catch bad URN values on optional n attribute for TEI title" in  {
    val str = "<title n=\"Not a URN:\">Iliad</title>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.ok(el) == false)
  }

  it should "include an errorMsg function reporting results of error checking" in {
    val str = "<title n=\"Not a URN:\">Iliad</title>"
    val el = XML.loadString(str)
    val  expected = "For TEI 'title', @n attribute is optional but must be a valid Cite2Urn if included."
    assert(HmtTeiAttributes.errorMsg(el).get == expected)
  }

  it should "report None in errorMsg for valid usage" in {
    val str = "<title n=\"urn:cite2:hmt:citedworks.v1:work1\">Iliad</title>"
    val el = XML.loadString(str)
    assert(HmtTeiAttributes.errorMsg(el) == None)
  }
}

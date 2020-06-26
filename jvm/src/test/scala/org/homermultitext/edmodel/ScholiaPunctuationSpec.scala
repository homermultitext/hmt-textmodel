package org.homermultitext.edmodel

import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.greek._

import edu.holycross.shot.mid.orthography._

class ScholiaPunctuationSpec extends FlatSpec {

  "The ScholiaOrthography object" should "recognize leading and trailing quotes in tokenizing" in  {
    val quoted = """ "νῆας" """
    val urn = CtsUrn("urn:cts:greekLit:tlg5026.msB.hmt:23.msB_303r_4")
    val cn = CitableNode(urn, quoted)

    val tokens = ScholiaOrthography.tokenizeNode(cn)
    assert(tokens.size == 3)

    val puncts = tokens.filter(t => t.tokenCategory.get == PunctuationToken)
    assert(puncts.size == 2)
    val  lex = tokens.filter(t => t.tokenCategory.get == LexicalToken)

    assert(lex.size == 1 )
    print(lex.head.text)
  }




}

package org.homermultitext.edmodel

import scala.xml._

object TeiReader {

  def teiToTokens(urnStr: String, xmlStr: String) : Vector[(String, HmtToken)]  = { //: Vector[HmtToken] = {
    val root  = XML.loadString(xmlStr)
    val currToken = HmtToken(
      urn = urnStr,
      lexicalCategory = LexicalToken,
      readings = Vector.empty
    )
    Vector(currToken).zipWithIndex.map{ case (t,i) => (urnStr + i, t) }
  }

/*
def tokenizePair(urnStr: String, xmlStr: String ) = {
  val root  = XML.loadString(xmlStr)
  val currToken = HmtToken(
    urn = urnStr,
    lexicalCategory = LexicalToken,
    txtV = Vector.empty
  )
  // zero out the global buffer,
  // then collect:
  tokenBuffer.clear
  collectTokens(currToken, root)
  var count = 1
  for (tk <- tokenBuffer) {
    tk.urn = tk.urn + "." + count
    count = count + 1
  }
  tokenBuffer.toVector
}*/
}

package org.homermultitext.edmodel


case class TextDeformation(text: String)
/**  Factory for Vectors of  [[org.homermultitext.edmodel.HmtOrcaToken]] instances.
*/
object TextDeformation {

  def readDiplomatic(tkn: HmtToken):  TextDeformation = {
    var buff = StringBuilder.newBuilder
    for (r <- tkn.readings) {
      r.status match {
        case Clear => buff.append(r.reading)
        case _ => // do nothing
      }
    }
    TextDeformation(buff.toString)
  }


  def apply(hmtToken : HmtToken, rdg: String): TextDeformation = {
    rdg match {
      case "dipl" => readDiplomatic(hmtToken)
      case _ => throw new Exception("TextDeformation: unrecognized or unimplemented reading key " + rdg)
    }
  }
}

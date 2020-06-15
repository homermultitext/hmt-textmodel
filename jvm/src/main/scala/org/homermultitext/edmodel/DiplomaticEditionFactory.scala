package org.homermultitext.edmodel



import edu.holycross.shot.ohco2._

/**  Factory to build a diplomatic edition from a Vector
* of [[TokenAnalysis]]s.
*/
object DiplomaticEditionFactory {


  /** Create a corpus of diplomatic editions from a vector of token analyses.
  *
  * @param tokens TokenAnalysis objects to work from.
  */
  def corpusFromTokens(tokens: Vector[TokenAnalysis]): Corpus = {
    val scribal = tokens.map(_.readWithDiplomatic)
    val diplomaticNodes = scribal.map( n => {
      n match {
        case None => None
        case  _ => {
          val node = n.get
          val baseVersion = node.urn.version
          val u = node.urn.dropVersion.addVersion(baseVersion + "_diplomatic")
          Some(CitableNode(u, node.text))
        }
      }
    })
    Corpus(diplomaticNodes.flatten)
  }

}

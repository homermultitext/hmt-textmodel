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
    val scribal = tokens.map(_.readWithScribal)
    val diplomaticNodes = scribal.map( n => {
      val baseVersion = n.urn.version
      val u = n.urn.dropVersion.addVersion(baseVersion + "_diplomatic")
      CitableNode(u, n.text) }
    )
    Corpus(diplomaticNodes)
  }

}

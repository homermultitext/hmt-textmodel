package org.homermultitext.edmodel

import edu.holycross.shot.cite._

/** a complete reading of a text expressed as an analytical exemplar
*
* @param title labelling string or title of edition
* @param tokens sequence of [[org.homermultitext.edmodel.HmtOrcaToken]]s
* defining an analytical edition
*/
case class HmtReading(title: String, description: String, tokens: Vector[HmtOrcaToken])

object HmtReading {

  /** build an HMT analytical edition from a set of analyses
  *
  * @param editionKey key value in map of configurations defined in package object
  * @param analyses full analyses of HMT tokens
  */
  def apply(editionKey: String, analyses: Vector[TokenAnalysis]): HmtReading = {
    if (exemplarLabels contains editionKey) {
      //val orcaTokens = Vector.empty[HmtOrcaToken]
      val orcaTokens = analyses.map(t => HmtOrcaToken(t.analysis, editionKey))
      // collect all works in supplied analyses:


      HmtReading(exemplarLabels(editionKey).title, exemplarLabels(editionKey).description, orcaTokens)



    } else {
      throw new Exception("HmtReading: no reading configured for key " + editionKey)
    }

  }
}

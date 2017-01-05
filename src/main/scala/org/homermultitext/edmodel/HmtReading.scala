package org.homermultitext.edmodel

import edu.holycross.shot.cite._

/** token in an ORCA analytical exemplar
*
* @param urn exemplar-level URN identifying this token in a specific reading of a
* HMT edition
* @param src URN of passage read or analyzed
* @param textDeformation string view of this token
* @param hmtToken full analysis of this token
*/
case class HmtOrcaToken (urn: CtsUrn,
  src: CtsUrn, textDeformation: String, hmtToken: HmtToken)

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
  def apply(editionKey: String, analyses: Vector[HmtToken]): HmtReading = {
    if (exemplarLabels contains editionKey) {
      val orcaTokens = Vector.empty[HmtOrcaToken]

      // collect all works in supplied analyses:


      HmtReading(exemplarLabels(editionKey).title, exemplarLabels(editionKey).description, orcaTokens)



    } else {
      throw new Exception("HmtReading: no reading configured for key " + editionKey)
    }

/*

   Source.fromFile("scholia-twocolumns.tsv").getLines.toVector.map(_.split("\t")).map( arr => (CtsUrn(arr(0)), arr(1)) )
      // extract list of distinct works:
      //val workList = pairs.map{ case(u,x) => u.workComponent }.distinct
    }
*/
  }
}

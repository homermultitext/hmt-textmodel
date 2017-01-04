package org.homermultitext.edmodel

import edu.holycross.shot.cite._

/** token in an ORCA analytical exemplar
*
* @param urn exemplar-level URN identifying this token in a specific reading of a
* HMT edition
* @param textDeformation string view of this token
* @param hmtToken full analysis of this token
*/
case class HmtEditionToken (urn: CtsUrn, textDeformation: String, hmtToken: HmtToken)


/** a complete reading of a text expressed as an analytical exemplar
*
* @param title labelling string or title of edition
* @param tokens sequence of [[org.homermultitext.edmodel.HmtEditionToken]]s
* defining an analytical edition
*/
case class HmtEdition(title: String, tokens: Vector[HmtEditionToken])

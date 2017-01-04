package org.homermultitext.edmodel


import edu.holycross.shot.cite._

case class HmtEditionToken (urn: CtsUrn, textDeformation: String, hmToken: HmtToken)

case class HmtEdition(title: String, tokens: Vector[HmtEditionToken])


package org.homermultitext

import java.text.Normalizer.Form
import java.text.Normalizer

package object edmodel {
  // perhaps should be a function retrieving
  // list by text group and lexical category?
  val punctuation = Vector(",",".",";","⁑")

  // only need list of elements *not* explicitly
  // caught in big case match below
  val validElements = Vector(
    "div", "l","p", "choice",
    "foreign",
    "num",
    "unclear","add","orig","reg","sic","corr",
    "abbr","expan",
    "cite","q","ref",
    "persName","placeName",
    "rs"
  )



  /** Recursively collects contents of all text-node
  * descendants of a given node.
  * @param n Node to collect from.
  * @param buff Buffer for collecting text contents.
  * @returns A single String with all text from n.
  */
  def collectText(n: xml.Node, s: String): String = {
    var buff = StringBuilder.newBuilder
    buff.append(s)
    n match {
      case t: xml.Text => {
        buff.append(t.text)
      }

      case e: xml.Elem => {
        for (ch <- e.child) {
          buff = new StringBuilder(collectText(ch, buff.toString))
        }
      }
    }
    buff.toString
  }


  def hmtNormalize(s: String): String = {
    Normalizer.normalize(s,Form.NFC).trim.replaceAll("[ ]+"," ")
  }
}

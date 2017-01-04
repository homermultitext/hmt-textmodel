

package org.homermultitext

package object edmodel {


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
}

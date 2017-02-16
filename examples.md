# Examples of usage



## Analyze a corpus of texts

Use the `ohco2` library to create a corpus of the whole HMT project:

    val corpus = CorpusSource.fromFile("data/hmtarchive_2cols.tsv")

Use URN twiddling to select the main scholia as a corpus:

    val mainScholia = corpus ~~ CtsUrn("urn:cts:greekLit:tlg5026.msA:")

Analyze the corpus. The result is a Vector of `TokenAnalysis` objects.

    val tokens = TeiReader.fromCorpus(mainScholia)

A `TokenAnalysis` has two members: a CTS URN identifying the text, and a `HmtToken` with analytical data.

The following examples illustrate ways to work with vectors of token analyses.


## Turn a vector analyses into a new text corpus citable by token

    val citableNodes = tokens.map(_.simpleText)
    val newCorpus = Corpus(citableNodes)



## Alternate readings

From a vector of analyses, select all the analyses that include an alternate reading from the diplomatic reading:

    val altTokens = tokens.filter(_.hasAlternate)

## Lexical disambiguation

Use URN twiddling to select all analyses with matching lexical disambiguation.  E.g., select all instances of personal name Achilles:

    val achilles = Cite2Urn("urn:cite2:hmt:pers:pers1")
    val achillesTokens = tokens.filter(_.lexMatch(achilles))



## Discourse analysis



## More

Read a citable passage in a HMT-compliant well-formed fragment of TEI XML, and return a vector of analyzed tokens:

    val urn = CtsUrn( "urn:cts:greekLit:tlg5026.msAil.hmt:1.1303.comment")
    val xml = """<div xmlns="http://www.tei-c.org/ns/1.0" n="comment"> <p> ἢ απαρνησαι</p></div>"""

    val tokens = TeiReader.teiToTokens(urn, xml)

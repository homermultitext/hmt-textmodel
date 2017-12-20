# Using the `hmt-textmodel` library



## Introduction

The `hmt-textmodel` for working with HMT text content abstracts a number of important features of the HMT text archive.  It relies on the generic `ohco2` library for two important  constructs:

1. a `Corpus` of texts, made from a Vector of `CitableNode`s.
2. the `CitableNode` object, a single citable passage of text with URN and text content


The top-level concept of the `hmt-textmodel` is the `TokenAnalysis`.  A `TokenAnalysis` has two members: a CTS URN identifying the text, and a complex `HmtToken` with analytical data. The `HmtToken` captures everything expressed about a token by HMT project XML editions, but the `TokenAnalysis`  has high-level functions (illustrated here) for working with its contents.  These functions either:

- *filter* a Vector of analyses to create a new Vector of analyses, or
- *transform* a Vector of analyses to a Vector of citable text nodes



## Overview: analyze a corpus of texts

Use the `ohco2` library to create a corpus of the whole HMT project:

    val corpus = CorpusSource.fromFile("data/hmtarchive_2cols.tsv")

Use URN twiddling to select the main scholia as a corpus:

    val mainScholia = corpus ~~ CtsUrn("urn:cts:greekLit:tlg5026.msA:")

Analyze the scholia. The result is a Vector of `TokenAnalysis` objects.

    val tokens = TeiReader.fromCorpus(mainScholia)




## Turn a vector of analyses into a new text corpus citable by token

Create a vector of text nodes using alternate readings, where they exist, rather than diplomatic readings, and make a text corpus from that:

    val citableNodes = tokens.map(_.readWithAlternate)
    val normalizedCorpus = Corpus(citableNodes)

Create a text corpus using purely diplomatic readings:

    val citableNodes = tokens.map(_.readWithDiplomatic)
    val diplomaticCorpus = Corpus(citableNodes)

## Filter analyses by alternate readings

From a vector of analyses, select all the analyses that include *any* category of alternate reading differing from the diplomatic reading:

    val altTokens = tokens.filter(_.hasAlternate)

Select all analyses that include an expanded abbrevation:

    val abbrTokens = tokens.filter(_.isAbbreviation)

Select all analyses that include a scribal correction:

    val corrTokens = tokens.filter(_.hasScribalCorrection)

Select all analyses that include a scribal multiform:

    val multiformTokens = tokens.filter(_.hasMultiform)


## Filter analyses by lexical disambiguation

Use URN twiddling to select all analyses with matching lexical disambiguation.  E.g., select all instances of personal name Achilles:

    val achilles = Cite2Urn("urn:cite2:hmt:pers:pers1")
    val achillesTokens = tokens.filter(_.lexMatch(achilles))



## Filter analyses by discourse analysis

Use these filters to select tokens by category of discourse:

    val directVoice = tokens.filter(_.isDirectVoice)
    val citations = tokens.filter(_.isCitation)
    val quotedText = tokens.filter(_.isQuotedText)
    val quotedLiteral = tokens.filter(_.isQuotedLiteral)
    val quotedLanguage = tokens.filter(_.isQuotedLanguage)
    val notDirect = tokens.filter(_.notDirectVoice)

## Combinations: create citable nodes for selected analyses

Find all analyses of tokens as citations, and create a vector of citable text nodes using expanded (alternate) readings rather than diplomatic view:

    val citations = tokens.filter(_.isCitation)
    val citationPassages = citations.map(_.readWithAlternate)

Since `citationPassages` is just a Vector of citable nodes, you can do normal Scala magic with it, e.g., create a string listing the text of each token on a single line:

    val citedList = citationPassages.map(_.text).mkString("\n")

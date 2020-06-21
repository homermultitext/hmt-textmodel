---
layout: page
title: "Scripting with the HMT text model"
---

Include the library:

```tut:silent
import org.homermultitext.edmodel._
```


The `TeiReader` object can read  a ctiable text in HMMT project XML, and produce a vector of analyses, where each analysis corresponds to a single token in the text.

The `TeiReader` has functions to read a file, or a `Corpus` in the `ohco2` library, but can also read a simple string with delimited text like this:

```

val iliadOpening = """urn:cts:greekLit:tlg0012.tlg001.va_xml:1.1#<l n="1" xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0">Μῆνιν ἄειδε θεὰ <persName n="urn:cite2:hmt:pers.r1:pers1">Πηληϊάδεω  Ἀχιλῆος</persName> </l>
urn:cts:greekLit:tlg0012.tlg001.va_xml:1.2#<l n="2" xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0">οὐλομένην· ἡ μυρί' <rs type="ethnic" n="urn:cite2:hmt:place.r1:place96">Ἀχαιοῖς</rs> ἄλγε' ἔθηκεν· </l>
urn:cts:greekLit:tlg0012.tlg001.va_xml:1.3#<l n="3" xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0">πολλὰς δ' ἰφθίμους ψυχὰς <placeName n="urn:cite2:hmt:place.r1:place67">Ἄϊδι</placeName> προΐαψεν </l>
urn:cts:greekLit:tlg0012.tlg001.va_xml:1.4#<l n="4" xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0">ἡρώων· αὐτοὺς δὲ ἑλώρια τεῦχε κύνεσσιν </l>
urn:cts:greekLit:tlg0012.tlg001.va_xml:1.5#<l n="5" xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0">οἰωνοῖσί τε πᾶσι· <persName n="urn:cite2:hmt:pers.r1:pers8">Διὸς</persName> δ'  ἐτελείετο βουλή· </l>
"""

val tokens = TeiReader.fromString(iliadOpening)
assert(tokens.size == 30)
```

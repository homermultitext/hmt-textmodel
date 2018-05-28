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

val iliadOpening = "urn:cts:greekLit:tlg0012.tlg001.msA:1.1#Μῆνιν ἄειδε θεὰ Πηληϊάδεω Ἀχιλῆος\nurn:cts:greekLit:tlg0012.tlg001.msA:1.2#οὐλομένην· ἡ μυρί' Ἀχαιοῖς ἄλγε' ἔθηκεν·\nurn:cts:greekLit:tlg0012.tlg001.msA:1.3#πολλὰς δ' ἰφθίμους ψυχὰς Ἄϊδι προΐαψεν\nurn:cts:greekLit:tlg0012.tlg001.msA:1.4#ἡρώων· αὐτοὺς δὲ ἑλώρια τεῦχε κύνεσσιν\nurn:cts:greekLit:tlg0012.tlg001.msA:1.5#οἰωνοῖσί τε πᾶσι· Διὸς δ' ἐτελείετο βουλή·\n"

val tokens = TeiReader.fromString(iliadOpening)
```

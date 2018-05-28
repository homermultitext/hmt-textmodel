---
layout: page
title: "Scripting with the HMT text model"
---

Include the library:

```tut:silent
import org.homermultitext.edmodel._
```


The `TeiReader` object can read  a ctiable text in HMMT project XML, and produce a vector of analyses.  Each analysis corresponds to a single token in the text.

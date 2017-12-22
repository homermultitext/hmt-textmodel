---
layout:  page
title:  HMT editions are citable
---


## Citation

The HMT project uses IETF-standard URN notation for all scholarly citation.  URNs are text strings with a specified syntax and semantics.  They are independent of any specific technology, can be parsed by human readers, and are machine-actionable.


The `hmt-textmodel` library depends on the CITE architecture's `cite` library for `CtsUrn` and `Cite2Urn` classes that you can instantiate from a string value.  These classes include numerous functions for manipulating the URN objects.

## Citing texts

The `hmt-textmodel` library uses the generic `ohco2` library for two important  constructs:

1.  a `Corpus` of texts, made from a Vector of `CitableNode`s.
2.  the `CitableNode` object, a single citable passage of text with URN and text content

## Citing objects

TBA

## See also



-   [editorial principles](https://homermultitext.github.io/hmt-editing-principles/citation) modeled by these libraries
-   the HMT editor's guide on [encoding citation](https://homermultitext.github.io/hmt-editors-guide/citation)

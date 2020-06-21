---
title: Analyzed tokens
layout: page
---


## Concepts

Reading a digital edition produces a sequence of text units with specified semantics.  In this documentation, we will refer to those units as *tokens*.


## Implementation

The top-level concept of the `hmt-textmodel` is the `TokenAnalysis`.  A `TokenAnalysis` has two members: a CTS URN identifying the text, and a complex `HmtToken` with analytical data. The `HmtToken` captures everything expressed about a single token by the XML documents of the HMT project, but the `TokenAnalysis`  has high-level functions (illustrated here) for working with collections of those tokens.  In general, these functions either:

-   *filter* a Vector of analyses to create a new Vector of analyses, or
-   *transform* a Vector of analyses into a Vector of citable text nodes

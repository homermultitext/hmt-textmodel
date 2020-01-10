# `hmt-textmodel`: release notes

**5.2.0**: add logging support.

**5.1.0**: update library dependencies

**5.0.1**:  Add lib dependency for transitively required evilplot

**5.0.0**:   API-breaking rewrite, now in 1<->1 relation with all HMT editing roles listed in this project's wiki.

**4.1.0**:  New functionality in underlying CITE libraries.

**4.0.0**:  API-breaking change. `readWithX` functions formerly returning `CitableNode`s now return `Option[CitableNode]`s.

**3.4.3**:   Includes character to encode "floating" circumflex.

**3.4.2**:  Correctly apply  HMT text normalization to alternate readings as well as main readings.


**3.4.1**:  Outrun horrific treatment of Unicode equivalencies of codepoints in the Greek range in the stand java `Normalizer` library while parsing HMT source XML.

**3.4.0**:  Add `hmtNormalize` function, and provide work around for erroneous NFC conversion in standard java library.

**3.3.0**:  Add `legalChars` and `badCPs` functions to `HmtChars`.

**3.2.0**:   Add `normalizeCPs` function in `HmtChars`.

**3.1.0**:   Adds `HmtChars` object.

**3.0.0**:  **API breaking change**.  `TeiReader` object largely replaced by case class.

**2.4.0**:  Add support for TEI `rs` element of type `astro`.

**2.3.2**:  Update  dependencies on multiple underlying libraries

**2.3.1**:  Makes model of `add` element work with `CitableNode`.

**2.3.0**:  Adds support for `add` and `del` elements in HMT XML.

**2.2.3**:  Bug fixes in updated library dependency.

**2.2.2**:  Handle tokenizing of unregistered source XML.

**2.2.1**:  Update  dependencies on multiple underlying libraries.

**2.2.0**:  Support for new editions in HMT 2018 releases.

**2.1.0**:  Further dependency updates introducing new functionality in underlying libraries.

**2.0.1**:  Update dependencies on multiple libraries.

**2.0.0**: API-breaking change to `TokenAnalysis` object;  update CITE references to `cite2`.

**1.4**:  Adds text-searching functions.

**1.3**:  Adds functions for analyzing and tokenizing from various sources.

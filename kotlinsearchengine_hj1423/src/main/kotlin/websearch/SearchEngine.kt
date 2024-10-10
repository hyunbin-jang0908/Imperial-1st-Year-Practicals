package websearch

// import org.jsoup.Jsoup

class SearchEngine(val map: Map<URL, WebPage>) {
  val index: MutableMap<String, List<SearchResult>> = mutableMapOf()

  fun compileIndex() {
    val allWordAndUrl: MutableList<Pair<String, URL>> = mutableListOf()
    for ((url, webpage) in map) {
      val extractedWords = webpage.extractWords()
      val wordUrlPair = extractedWords.map { Pair(it, url) }
      allWordAndUrl += wordUrlPair
    }
    val groupedByWord = allWordAndUrl.groupBy({ it.first }, { it.second })
    for ((word, url) in groupedByWord) {
      index[word] = rank(url)
    }
  }

  fun searchFor(query: String): SearchResultSummary {
    val lookedUp = index.getOrElse(query) { emptyList() } // List of SearchResults or null value
    return SearchResultSummary(query, lookedUp)
  }
}

fun rank(urls: List<URL>): List<SearchResult> =
  urls.groupBy { it }.map { (url, urlList) -> SearchResult(url, urlList.size) }.sortedByDescending { it.numRefs }

class SearchResult(val url: URL, val numRefs: Int) {
  override fun toString(): String = "$url - $numRefs references"
}

class SearchResultSummary(val query: String, val results: List<SearchResult>) {
  override fun toString(): String {
    var formatted = ""
    return if (results.isEmpty()) {
      "Your search \"$query\" did not match any documents."
    } else {
      for (sr in results) {
        formatted += "    $sr\n"
      }
      "Results for \"$query\":\n$formatted"
    }
  }
}

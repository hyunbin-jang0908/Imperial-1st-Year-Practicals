package websearch

// import org.jsoup.Jsoup
// import org.jsoup.nodes.Document

fun main() {
  val crawler = WebCrawler(startURL = URL("https://www.bbc.co.uk"))
  crawler.run()

  val searchEngine = SearchEngine(crawler.dump())
  searchEngine.compileIndex()

  println(searchEngine.searchFor("news"))
}

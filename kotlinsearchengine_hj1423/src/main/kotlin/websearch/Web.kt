package websearch

import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

// import javax.print.Doc

class URL(val url: String) {
  override fun toString(): String = url

  override fun equals(other: Any?): Boolean = (other is URL) && (this.url == other.url)

  override fun hashCode(): Int = url.hashCode()

  fun download(): WebPage {
    try {
      val downloadedDocument = Jsoup.connect(url).get()
      return WebPage(downloadedDocument)
    } catch (e: HttpStatusException) {
      println("$url has caused a HttpStatusException.")
      throw Exception("Failed to download the page.")
    }
  }
}

class WebPage(val doc: Document) {
  fun extractWords(): List<String> = doc.text().lowercase().filter { it !in listOf('.', ',') }.split(" ")

  fun extractLinks(): List<URL> {
    val aTags = doc.getElementsByTag("a")
    return aTags.map { URL(it?.attr("href") ?: "") }.distinct()
      .filter { it.url.startsWith("http://") || it.url.startsWith("https://") }
  }
}

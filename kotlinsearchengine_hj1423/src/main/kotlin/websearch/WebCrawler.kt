package websearch

import java.lang.Exception

class WebCrawler(val startURL: URL) {
  private val maxPages = 15
  private val pagesDownloaded = mutableMapOf<URL, WebPage>()

  // Version using for loop and recursion. More inefficient.
  //  fun run(pagesDownloaded: MutableMap<URL, WebPage> = mutableMapOf()): Map<URL, WebPage> {
//    if (startURL !in pagesDownloaded) pagesDownloaded[startURL] = startURL.download()
//    val extractedLinks = startURL.download().extractLinks()
//    // println("extracted Links: $extractedLinks")
//
//    for (url in extractedLinks) {
//      if (pagesDownloaded.size == maxPages) return pagesDownloaded
//      if (url in pagesDownloaded) continue
//      pagesDownloaded[url] = url.download()
//    }
//
//    for (url in extractedLinks) {
//      if (pagesDownloaded.size == maxPages) return pagesDownloaded
//      pagesDownloaded += WebCrawler(url).run(pagesDownloaded) // overwrites
//    }
//    return pagesDownloaded
//  }
  // Version using queue instead of recursion.
  fun run() {
    val failedUrls = mutableListOf<URL>() // List of URLs that caused an error.
    val queue = ArrayDeque<URL>()         // Queue for URLs to be downloaded

    queue.add(startURL) // Add the start URL to the queue

    // Continue until the queue is empty or we have downloaded enough pages
    while (queue.isNotEmpty() && pagesDownloaded.size < maxPages) {
      val url = queue.removeFirst()      // Remove the first URL from the queue

      // If the URL has not been downloaded yet
      if (url !in pagesDownloaded && url !in failedUrls) {
        try {
          val page = url.download()
          pagesDownloaded[url] = page
          val extractedLinks = page.extractLinks()
          // Add the extracted links to the queue if they have not been downloaded yet
          queue.addAll(extractedLinks.filter { it !in pagesDownloaded && it !in failedUrls })
        } catch (e: Exception) {
          failedUrls += url
          println("$url has caused Exception.")
          continue
        }
      }
    }
  }

  fun dump(): Map<URL, WebPage> = pagesDownloaded
}

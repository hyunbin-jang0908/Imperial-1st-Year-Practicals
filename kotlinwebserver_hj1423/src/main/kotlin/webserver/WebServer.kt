package webserver

// write your web framework code here:

fun scheme(url: String): String = url.substringBefore("://")

fun host(url: String): String = url.substringAfter("://").substringBefore("/")

fun path(url: String): String = url.substringAfter(host(url)).substringBefore("?")

fun queryParams(url: String): List<Pair<String, String>> {
//  val query = url.substringAfter("?")
//  val tempList = query.split("&")
//  var list: List<Pair<String, String>> = mutableListOf()
//  return if (query == url) {
//    list
//  } else {
//    for (pair in tempList) {
//      val name = pair.split("=")[0]
//      val value = pair.split("=")[1]
//      list = list + Pair(name, value)
//    }
//    list
//  }
  if (!url.contains("?")) return emptyList()

  return url.substringAfter("?").split("&")
    .map {Pair(it.substringBefore("="), it.substringAfter("="))}
}


// http handlers for a particular website...

fun helloHandler(request: Request): Response {
  val params = queryParams(request.url)
  val nameTuple = params.find {(k, v) -> k == "name"}
  val styleTuple = params.find {(k, v) -> k == "style"}
  val name = if(nameTuple == null) "World" else nameTuple.second
  return if (styleTuple?.second == "shouting") Response(Status.OK, "HELLO, ${name.uppercase()}!")
  else Response(Status.OK, "hello, $name.")
}

fun homePageHandler(request: Request): Response = Response(Status.OK, "This is Imperial.")

fun docHandler(request: Request): Response = Response(Status.OK, "This is DoC.")

fun route(request: Request): Response {
  val path = path(request.url)
  println(path)
  return when (path) {
      "/say-hello" -> helloHandler(request)
      "/"          -> homePageHandler(request)
      "/computing" -> docHandler(request)
      else         -> Response(Status.NOT_FOUND)
  }
}

fun main() {
  val request = Request("http://www.imperial.ac.uk/say-hello?name=Hyunbin")
  println(route(request).body)
}


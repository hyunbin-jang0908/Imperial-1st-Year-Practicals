package webapp

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.body.form
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.http4k.template.FreemarkerTemplates
import database.Post
import database.PostsDatabase
import org.http4k.core.Status.Companion.FOUND

val app: HttpHandler = routes(

    "/index" bind GET to {
        val renderer = FreemarkerTemplates().HotReload("src/main/resources")
        val viewModel = IndexPage("Ok")
        Response(OK).body(renderer(viewModel))
    },

    "/assets" bind static(ResourceLoader.Directory("src/main/resources/assets")),

    "/submit" bind POST to { request ->
      val input1 = request.form("input1") ?: ""
      val input2 = request.form("input2") ?: ""
      val input3 = request.form("input3") ?: ""
      val input4 = request.form("input4") ?: ""
      val input5 = request.form("input5") ?: ""

      // Calculate Scrabble scores
      var scoreList = mutableListOf(
        input1 to calculateScrabbleScore(input1),
        input2 to calculateScrabbleScore(input2),
        input3 to calculateScrabbleScore(input3),
        input4 to calculateScrabbleScore(input4),
        input5 to calculateScrabbleScore(input5)
      )
      scoreList = scoreList.sortedByDescending { it.second }.toMutableList()
      val firstPair = scoreList[0]
      val secondPair = scoreList[1]
      val thirdPair = scoreList[2]
      val fourthPair = scoreList[3]
      val fifthPair = scoreList[4]

      // Render the response
      val renderer = FreemarkerTemplates().HotReload("src/main/resources")
      val viewModel = SubmitPage(firstPair.first, secondPair.first, thirdPair.first, fourthPair.first, fifthPair.first,
                                firstPair.second, secondPair.second, thirdPair.second, fourthPair.second, fifthPair.second)
      Response(OK).body(renderer(viewModel))
    },

    "/post" bind POST to { request ->

      val title = request.form("title") ?: ""
      val content = request.form("content") ?: ""
      val postDB = PostsDatabase()

      if (title.isNotBlank() || content.isNotBlank()) {
        val newPost = Post(title = title, body = content)
        postDB.addPost(newPost)
      }

      Response(FOUND).header("Location", "/post")
    },

    "/post" bind GET to {
      val renderer = FreemarkerTemplates().HotReload("src/main/resources")
      val postDB = PostsDatabase()

      val posts = postDB.loadAllPosts()
      val viewModel = PostsPage(posts)
      Response(OK).body(renderer(viewModel))
    },
)

fun calculateScrabbleScore(word : String): Int {
  val scoreMap = mapOf(
    'A' to 1, 'B' to 3, 'C' to 3, 'D' to 2, 'E' to 1,
    'F' to 4, 'G' to 2, 'H' to 4, 'I' to 1, 'J' to 8,
    'K' to 5, 'L' to 1, 'M' to 3, 'N' to 1, 'O' to 1,
    'P' to 3, 'Q' to 10, 'R' to 1, 'S' to 1, 'T' to 1,
    'U' to 1, 'V' to 4, 'W' to 4, 'X' to 8, 'Y' to 4,
    'Z' to 10, ' ' to 0
  )
  var sum = 0
  for (char in word) {
    sum += scoreMap[char.uppercaseChar()] ?: 0
  }
  return sum
}

fun main() {
    val server = app.asServer(Jetty(9000)).start()
    println("Server started on http://localhost:" + server.port() + "/index")
}

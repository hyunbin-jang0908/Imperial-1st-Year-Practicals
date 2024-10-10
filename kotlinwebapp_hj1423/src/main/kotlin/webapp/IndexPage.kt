package webapp

import database.Post
import org.http4k.template.ViewModel

data class IndexPage(val self: String = "yes") : ViewModel {
    override fun template() = "views/IndexPage.ftl"
}

data class SubmitPage(val input1: String = "", val input2: String = "",
                      val input3: String = "", val input4: String = "", val input5: String = "",
                      val score1: Int, val score2: Int,
                      val score3: Int, val score4: Int, val score5: Int) : ViewModel {
  override fun template() = "views/SubmitPage.ftl"
}

data class PostsPage(val posts:List<Post>) : ViewModel {
  override fun template() = "views/PostsPage.ftl"
}


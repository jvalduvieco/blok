package com.blok.Post.Functional
import com.blok.common.DatabaseSetup
import com.blok.model.Comment
import com.blok.post.Infrastructure.PostgresPostRepositoryForTesting
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import cucumber.api.java.Before
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import org.apache.http.HttpStatus
import org.junit.Assert.assertTrue
import java.util.*

class CommentStepDefinitions {
    private val postRepository = PostgresPostRepositoryForTesting(DatabaseSetup())
    @Before
    fun cleanDatabase() {
        postRepository.cleanDatabase()
    }
    @When("^I add a comment to post ([a-f0-9-]+) by author=\"([^\"]*)\" with content=\"([^\"]*)\"$")
    @Throws(Throwable::class)
    fun iAddACommentWithContent(postId: UUID, author: String, content: String) {
        val payload: String = """
  {
    "author" : "$author",
    "content" : "$content"
  }
  """
        val response: String = post("http://localhost:4567/posts/${postId}/comments", payload)
        assertTrue(parseObjectCreationResponse(response) is UUID)
    }
    @Then("^the post ([a-f0-9-]+) has (\\d+) comment$")
    fun thePostHasComments(postId: UUID, numberOfComments: Int) {
        val comments: List<Comment> = get("http://localhost:4567/posts/${postId}/comments")
        assertThat(numberOfComments, equalTo(comments.count()))
    }
    @Then("^the post ([a-f0-9-]+) has a comment with content \"([^\"]*)\"$")
    fun aCommentWithContentExists(postId: UUID, content: String) {
        val comments: List<Comment> = get("http://localhost:4567/posts/${postId}/comments")
        val commentsThatMatch: List<Comment> = comments.filter { comment -> comment.content == content}
        assertThat(1, equalTo(commentsThatMatch.count()))
    }
    @When("^I add comments to a non existent post 404 is given$")
    fun iAddCommentsToANonExistingPost() {
        val payload: String = """
  {
    "author" : "Peter",
    "content" : "and the wolf"
  }
  """
        try {
            post("http://localhost:4567/posts/${UUID.randomUUID()}/comments", payload)
        } catch (e: FailedRequest) {
            assertThat(HttpStatus.SC_NOT_FOUND, equalTo(e.statusCode))
        }
    }
    @When("^I delete one comment of post ([a-f0-9-]+)$")
    fun iDeleteOneCommentOf(postId: UUID) {
        val comments: List<Comment> = get("http://localhost:4567/posts/${postId}/comments")
        delete("http://localhost:4567/posts/${postId}/comments/${comments.first().id}")
    }
    @When("^I delete a non existent comment of post ([a-f0-9-]+) 404 is given$")
    fun iDeleteANonExistentComment(postId: UUID) {
        try {
            delete("http://localhost:4567/posts/${postId}/comments/" + UUID.randomUUID())
        } catch (e: FailedRequest) {
            assertThat(HttpStatus.SC_NOT_FOUND, equalTo(e.statusCode))
        }
    }
}
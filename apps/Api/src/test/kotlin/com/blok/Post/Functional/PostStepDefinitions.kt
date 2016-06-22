package com.blok.Post.Functional

import com.blok.common.DatabaseSetup
import com.blok.model.Post
import com.blok.model.PostId
import com.blok.post.Infrastructure.PostgresPostRepositoryForTesting
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import cucumber.api.java.Before
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import org.apache.http.HttpStatus
import org.junit.Assert.assertTrue
import java.time.LocalDateTime
import java.util.*

class PostStepDefinitions {
    private val postRepository = PostgresPostRepositoryForTesting(DatabaseSetup())

    @Before
    fun cleanDatabase() {
        postRepository.cleanDatabase()
    }

    @Given("^that on the DB there is a post with UUID=([a-f0-9-]+) title=\"([^\"]*)\" content=\"([^\"]*)\"$")
    @Throws(Throwable::class)
    fun thatOnTheDBThereIsAPostWithUUIDTitleContent(id: PostId, title: String, content: String) {
        postRepository.save(Post(id, title, content, LocalDateTime.now()))
    }

    @When("^I insert a post with title \"([^\"]*)\" and content \"([^\"]*)\"$")
    @Throws(Throwable::class)
    fun iInsertAPostWithTitleAndContent(title: String, content: String) {
        val payload: String = """
  {
    "title" : "$title",
    "content" : "$content",
    "categories" : []
  }
  """
        val response: String = post("http://localhost:4567/posts", payload);
        assertTrue(parseObjectCreationResponse(response) is UUID)
    }
    @Then("^I have (\\d+) posts$")
    @Throws(Throwable::class)
    fun iHavePosts(numberOfPosts: Int) {
        val posts: List<Post> = get("http://localhost:4567/posts")
        assertThat(numberOfPosts, equalTo(posts.count()))
    }
    @Then("^the post has title \"([^\"]*)\"$")
    @Throws(Throwable::class)
    fun thePostHasTitle(title: String) {
        val posts: List<Post> =  get("http://localhost:4567/posts")
        assertThat(title, equalTo(posts.first().title))
    }

    @Then("^the post has content \"([^\"]*)\"$")
    @Throws(Throwable::class)
    fun thePostHasContent(content: String) {
        val posts: List<Post> = get("http://localhost:4567/posts")
        assertThat(content, equalTo(posts.first().content))
    }

    @Then("^the post ([a-f0-9-]+) has title \"([^\"]*)\"$")
    @Throws(Throwable::class)
    fun thePostHasTitle(id: UUID, title: String) {
        val post: Post = get("http://localhost:4567/posts/${id}")
        assertThat(title, equalTo(post.title))
    }

    @Then("^the post ([a-f0-9-]+) has content \"([^\"]*)\"$")
    @Throws(Throwable::class)
    fun thePostHasContent(id: UUID, content: String) {
        val post: Post = get("http://localhost:4567/posts/${id}")
        assertThat(content, equalTo(post.content))
    }

    @When("^I edit post ([a-f0-9-]+) setting title=\"([^\"]*)\" and content=\"([^\"]*)\"$")
    @Throws(Throwable::class)
    fun iEditPostSettingTitle(id: UUID, title: String, content: String) {
        val payload: String = """
  {
    "title" : "$title",
     "content" : "$content",
    "categories" : []
  }
  """
        put("http://localhost:4567/posts/${id}", payload)
    }

    @When("^I delete post ([a-f0-9-]+)$")
    @Throws(Throwable::class)
    fun iDeletePost(id: UUID) {
        delete("http://localhost:4567/posts/${id}")
    }

    @Then("^post ([a-f0-9-]+) is not found$")
    @Throws(Throwable::class)
    fun postIsNotFound(id: UUID) {
        try {
            val foo: Any = get("http://localhost:4567/posts/${id}")
        } catch (e: FailedRequest) {
            assertThat(HttpStatus.SC_NOT_FOUND, equalTo(e.statusCode))
        }
    }
    @When("^I send an invalid JSON to /posts I get a 401 response$")
    fun iSendAnInvalidJSONIGetResponse()
    {
        val payload: String = """
  {
    "title" : "Let's break the system",
  }
  """
        try {
            post("http://localhost:4567/posts", payload)
        } catch (e: FailedRequest) {
            assertThat(HttpStatus.SC_BAD_REQUEST, equalTo(e.statusCode))
        }
    }
    @When("^I send an incomplete JSON to /posts U get a 401 response$")
    fun iSendAnIncompleteJSONToPosts() {
        val payload: String = """
  {
    "title" : "Let's break the system"
  }
  """
        try {
            post("http://localhost:4567/posts", payload)
        } catch(e: FailedRequest) {
            assertThat(HttpStatus.SC_BAD_REQUEST, equalTo(e.statusCode))
        }
    }
    @When("^I fetch an nonexistent post I get a 404 response$")
    fun iFetchANonexistentPostIGet404() {
        val postId: UUID = UUID.randomUUID()
        try {
            get<Any>("http://localhost:4567/posts/${postId}")
        } catch (e: FailedRequest) {
            assertThat(HttpStatus.SC_NOT_FOUND, equalTo(e.statusCode))
        }
    }
}

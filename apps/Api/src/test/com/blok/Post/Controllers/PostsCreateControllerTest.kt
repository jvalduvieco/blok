package com.blok.Post.Controllers

import com.blok.Answer
import com.blok.common.Bus.CommandBus
import com.blok.common.Bus.SynchronousCommandBus
import com.blok.common.DatabaseSetup
import com.blok.handlers.PostsCreateController
import com.blok.handlers.RequestPayload.NewPost
import com.blok.model.PostId
import com.blok.model.PostRepository
import com.blok.post.CreatePostCommandHandler
import com.blok.post.Infrastructure.PostgresPostRepositoryForTesting
import com.blok.post.PostService
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PostsCreateControllerTest {
    val commandBus: CommandBus = SynchronousCommandBus()
    val postRepositoryForTesting: PostgresPostRepositoryForTesting = PostgresPostRepositoryForTesting(DatabaseSetup())
    val postRepository: PostRepository = postRepositoryForTesting
    val postService: PostService = PostService(postRepository)

    @Before
    fun setUp() {
        postRepositoryForTesting.cleanDatabase()
        commandBus.registerHandler(CreatePostCommandHandler(postService))
    }
    @Test
    fun iCanCallCreatePostController () {
        val response: Answer =
                PostsCreateController(commandBus)
                        .process(
                                NewPost("A title", "A content", listOf("popo", "pipi")),
                                hashMapOf(),
                                false)

        assertEquals(201, response.code)
        assertTrue(Gson().fromJson(response.body, PostId::class.java) is PostId)
    }
}
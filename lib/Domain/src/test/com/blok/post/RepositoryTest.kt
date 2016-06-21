package test.com.blok.post

import com.blok.common.DatabaseSetup
import com.blok.model.Comment
import com.blok.model.CommentId
import com.blok.model.Post
import com.blok.model.PostId
import com.blok.post.Infrastructure.PostgresPostRepositoryForTesting
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class RepositoryTest {
    private val postRepository = PostgresPostRepositoryForTesting(DatabaseSetup())
    @Before
    fun cleanDatabase() {
        postRepository.cleanDatabase()
    }
    @Test
    fun iCanCreateAPost() {
        val post: Post = Post(PostId("1373b661-efc9-47a7-938d-088183662c42"), "lorem", "ipsum", LocalDateTime.now(), listOf("latin", "phrases", "test"))
        postRepository.save(post)
        val postFromDatabase: Post = postRepository.getPost(PostId("1373b661-efc9-47a7-938d-088183662c42"))
        assertEquals(post.id, postFromDatabase.id)
        assertEquals(post.title, postFromDatabase.title)
        assertEquals(post.content, postFromDatabase.content)
        assertEquals(post, postFromDatabase)
    }
    @Test
    fun iCanCreateAPostWithNoCategories() {
        val post: Post = Post(PostId("1373b661-efc9-47a7-938d-088183662c42"), "lorem", "ipsum", LocalDateTime.now(), listOf())
        postRepository.save(post)
        val postFromDatabase: Post = postRepository.getPost(PostId("1373b661-efc9-47a7-938d-088183662c42"))
        assertEquals(post.id, postFromDatabase.id)
        assertEquals(post.title, postFromDatabase.title)
        assertEquals(post.content, postFromDatabase.content)
        assertEquals(post, postFromDatabase)
    }
    @Test
    fun iCanEditAPost() {
        val post: Post = Post.create("A title", "Lorem Ipsum", listOf())
        postRepository.save(post)
        val editedPost = post.copy(title = "A better title")
        postRepository.save(editedPost)
        val postFromDatabase: Post = postRepository.getPost(post.id)
        assertEquals(postFromDatabase.title, editedPost.title)
    }
    @Test
    fun iCanEditAPostWithCategories() {
        val post: Post = Post.create("A title", "Lorem Ipsum", listOf("lorem","ipsum"))
        postRepository.save(post)
        val editedPost = post.copy(title = "A better title", categories = listOf("lorem","latin"))
        postRepository.save(editedPost)
        val postFromDatabase: Post = postRepository.getPost(post.id)
        assertEquals(postFromDatabase.title, editedPost.title)
        assertEquals(postFromDatabase.categories, editedPost.categories)
    }
    @Test
    fun iCanCreateAComment() {
        val post: Post = Post(PostId("1373b661-efc9-47a7-938d-088183662c42"), "lorem", "ipsum", LocalDateTime.now(), listOf("latin", "phrases", "test"))
        postRepository.save(post)
        val comment: Comment = Comment(CommentId(), PostId("1373b661-efc9-47a7-938d-088183662c42"),"lucius", "lorem", LocalDateTime.now(),false)
        postRepository.save(comment)
        val commentFromDatabase: Comment = postRepository.getAllCommentsOn(PostId("1373b661-efc9-47a7-938d-088183662c42")).first()
        assertEquals(comment.post_id, commentFromDatabase.post_id)
        assertEquals(comment.id, commentFromDatabase.id)
        assertEquals(comment.content, commentFromDatabase.content)
        assertEquals(comment.submissionDate, commentFromDatabase.submissionDate)
        assertEquals(comment.approved, commentFromDatabase.approved)
        assertEquals(comment.author, commentFromDatabase.author)
        assertEquals(comment, commentFromDatabase)
    }
    @Test
    fun iCanDeleteAPost() {
        val post: Post = Post.create("title", "author", listOf("lorem", "ipsum"))
        postRepository.save(post)
        postRepository.deletePost(post.id)
        assertFalse(postRepository.existPost(post.id))
    }
    @Test
    fun iCanDeleteAPostWithComments() {
        val post: Post = Post.create("title", "author", listOf("lorem", "ipsum"))
        val comment: Comment = post.addComment("lorem", "author")
        postRepository.save(post)
        postRepository.save(comment)
        postRepository.deletePost(post.id)
        assertFalse(postRepository.existPost(post.id))
        assertTrue(postRepository.getAllCommentsOn(post.id).isEmpty())
    }

}
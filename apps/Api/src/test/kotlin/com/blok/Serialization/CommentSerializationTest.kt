package test.com.blok.post

import com.blok.model.Comment
import com.blok.model.Post
import com.blok.model.PostId
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class CommentSerializationTest {
    val serializer: Gson = Gson()
    @Test
    fun iCanSerialize() {
        val post: Post = Post(PostId("91ff2946-187e-4114-a185-712600ef1622"), "lorem", "ipsum", LocalDateTime.now(), listOf("latin", "phrases", "test"))
        val comment: Comment = post.addComment("siquatur","lucius")
        val jsonText: String = serializer.toJson(comment)
        val commentDeserialized: Comment = serializer.fromJson(jsonText, Comment::class.java)
        assertEquals(comment.id(), commentDeserialized.id())
        assertEquals(comment.post_id(), commentDeserialized.post_id())
        assertEquals(comment.author, commentDeserialized.author)
        assertEquals(comment.content, commentDeserialized.content)
        assertEquals(comment.submissionDate,commentDeserialized.submissionDate)
        assertEquals(comment.approved, commentDeserialized.approved)
    }
}

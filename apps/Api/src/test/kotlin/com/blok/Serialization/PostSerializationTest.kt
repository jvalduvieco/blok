package test.com.blok.post
import com.blok.model.Post
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class PostSerializationTest {
    val serializer: Gson = Gson()
    @Test
    fun iCanDeserializeAPost() {
        val post: Post = Post.create("lorem", "ipsum", listOf("latin", "phrases", "test"))
        val jsonText: String = serializer.toJson(post)
        val postDeserialized: Post = serializer.fromJson(jsonText, Post::class.java)
        assertEquals(post, postDeserialized)
        assertEquals(post.id(), postDeserialized.id())
        assertEquals(post.title, postDeserialized.title)
        assertEquals(post.content, postDeserialized.content)
        assertEquals(post.categories, postDeserialized.categories)
        assertEquals(post.publishingDate, postDeserialized.publishingDate)
    }
}
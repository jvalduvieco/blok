package com.blok.model

import com.blok.common.AbstractIdentifier
import java.time.LocalDateTime
import java.util.*


data class Post(
        val id: PostId,
        val title: String,
        val content: String,
        val publishingDate: LocalDateTime,
        val categories: List<String> = listOf<String>()
) {
    companion object {
        fun create(title: String, content: String, categories: List<String>): Post {
            return Post(PostId(), title, content, LocalDateTime.now(), categories)
        }
        fun create(postId: PostId, title: String, content: String, categories: List<String>): Post {
            return Post(postId, title, content, LocalDateTime.now(), categories)
        }
    }
    fun addComment(content: String, author: String): Comment {
        return Comment(CommentId(), this.id, author, content, LocalDateTime.now(), false)
    }
}

class PostId(id: UUID = UUID.randomUUID()) : AbstractIdentifier(id){
    constructor(anId: String?): this(UUID.fromString(anId))
}
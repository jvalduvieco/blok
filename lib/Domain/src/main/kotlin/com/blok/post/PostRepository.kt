package com.blok.model

interface PostRepository : AbstractRepository {
    fun save(post: Post): PostId
    fun save(comment: Comment): CommentId
    fun update(post: Post)
    fun listAllPosts(): List<Post>
    fun getAllCommentsOn(id: PostId): List<Comment>
    fun existPost(id: PostId): Boolean
    fun getPost(id: PostId): Post
    fun deletePost(id: PostId)
    fun deleteComment(id: CommentId)

    class PostNotFound(id: PostId) : AbstractRepository.NotFound(id)
    class CommentNotFound(id: CommentId) : AbstractRepository.NotFound(id)
}

interface PostRepositoryForTesting {
    fun cleanDatabase()
}
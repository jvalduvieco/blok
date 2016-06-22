package com.blok.post

import com.blok.model.*

class PostService(val postRepository: PostRepository) {
    fun createPost(post: Post) {
        postRepository.save(post)
    }
    fun updatePost(post: Post) {
        postRepository.update(post)
    }
    fun deletePost(postId: PostId) {
        postRepository.deletePost(postId)
    }
    fun addComment(postId: PostId, content: String, author: String) {
        val post: Post = postRepository.getPost(postId)
        val comment: Comment = post.addComment(content, author)
        postRepository.save(comment)
    }
    fun deleteComment(commentId: CommentId) {
        postRepository.deleteComment(commentId)
    }
    fun listAllPosts(): List<Post> {
        return postRepository.listAllPosts()
    }
    fun listAllComments(postId: PostId): List<Comment> {
        return postRepository.getAllCommentsOn(postId)
    }

    fun getPost(postId: PostId): Post {
        return postRepository.getPost(postId)
    }
}
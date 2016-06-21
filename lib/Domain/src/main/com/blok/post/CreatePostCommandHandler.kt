package com.blok.post

import com.blok.common.Bus.AbstractCommandHandler
import com.blok.model.Post

class CreatePostCommandHandler(val postService: PostService):
        AbstractCommandHandler<CreatePostCommand>(CreatePostCommand::class)  {
    override fun execute(command: CreatePostCommand) {
        val post: Post = Post.create(command.postId, command.title, command.content, command.categories)
        postService.createPost(post)
    }
}


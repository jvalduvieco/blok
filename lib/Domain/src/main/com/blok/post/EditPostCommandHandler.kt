package com.blok.post

import com.blok.common.Bus.AbstractCommandHandler
import com.blok.model.Post

class EditPostCommandHandler(val postService: PostService):
        AbstractCommandHandler<EditPostCommand>(EditPostCommand::class)  {
    override fun execute(command: EditPostCommand) {
        val post: Post = Post.create(command.postId, command.title, command.content, command.categories)
        postService.updatePost(post)
    }
}
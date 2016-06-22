package com.blok.post

import com.blok.common.Bus.AbstractCommandHandler

class DeletePostCommandHandler(val postService: PostService):
        AbstractCommandHandler<DeletePostCommand>(DeletePostCommand::class)  {
    override fun execute(command: DeletePostCommand) {
        postService.deletePost(command.postId)
    }
}
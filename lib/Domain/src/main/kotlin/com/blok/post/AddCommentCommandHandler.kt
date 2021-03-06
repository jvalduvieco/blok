package com.blok.post

import com.blok.common.Bus.AbstractCommandHandler

class AddCommentCommandHandler(val postService: PostService):
        AbstractCommandHandler<AddCommentCommand>(AddCommentCommand::class)  {
    override fun execute(command: AddCommentCommand) {
        postService.addComment(command.postId, command.content, command.author)
    }
}
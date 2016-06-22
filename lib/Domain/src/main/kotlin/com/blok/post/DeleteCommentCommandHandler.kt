package com.blok.post

import com.blok.common.Bus.AbstractCommandHandler

class DeleteCommentCommandHandler(val postService: PostService):
        AbstractCommandHandler<DeleteCommentCommand>(DeleteCommentCommand::class)  {
    override fun execute(command: DeleteCommentCommand) {
        postService.deleteComment(command.commentId)
    }
}
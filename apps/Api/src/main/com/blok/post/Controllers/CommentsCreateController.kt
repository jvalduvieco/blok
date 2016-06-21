package com.blok.handlers

import com.blok.Answer
import com.blok.CommandRequestHandler
import com.blok.Common.Infrastructure.toJson
import com.blok.common.Bus.CommandBus
import com.blok.handlers.RequestPayload.NewComment
import com.blok.model.CommentId
import com.blok.model.PostId
import com.blok.post.AddCommentCommand

class CommentsCreateController(bus: CommandBus) : CommandRequestHandler<NewComment>(NewComment::class.java, bus) {

    override fun processImpl(value: NewComment, urlParams: Map<String, String>, shouldReturnHtml: Boolean): Answer {
        val postId = PostId(urlParams[":post_id"] ?: throw IllegalArgumentException())
        val commentId: CommentId = CommentId()
        bus.execute(AddCommentCommand(postId, commentId, value.content, value.author))

        return Answer.created(commentId.toJson())
    }
}
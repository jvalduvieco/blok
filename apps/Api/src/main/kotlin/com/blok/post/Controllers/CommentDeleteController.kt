package com.blok.handlers

import com.blok.Answer
import com.blok.CommandRequestHandler
import com.blok.common.Bus.CommandBus
import com.blok.handlers.RequestPayload.NoPayload
import com.blok.model.CommentId
import com.blok.post.DeleteCommentCommand
import org.apache.http.HttpStatus

class CommentDeleteController(bus: CommandBus) : CommandRequestHandler<NoPayload>(NoPayload::class.java, bus) {

    override fun processImpl(value: NoPayload, urlParams: Map<String, String>, shouldReturnHtml: Boolean): Answer {
        val commentId = CommentId(urlParams[":comment_id"]?:throw IllegalArgumentException())
        bus.execute(DeleteCommentCommand(commentId))

        return Answer.ok()
    }
}
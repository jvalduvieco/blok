package com.blok.handlers

import com.blok.Answer
import com.blok.CommandRequestHandler
import com.blok.common.Bus.CommandBus
import com.blok.handlers.RequestPayload.NoPayload
import com.blok.model.PostId
import com.blok.post.DeletePostCommand

class PostsDeleteController(bus: CommandBus) : CommandRequestHandler<NoPayload>(NoPayload::class.java, bus) {

    override fun processImpl(value: NoPayload, urlParams: Map<String, String>, shouldReturnHtml: Boolean): Answer {
        val postId = PostId(urlParams[":post_id"] ?: throw IllegalArgumentException())
        bus.execute(DeletePostCommand(postId))

        return Answer.ok()
    }
}

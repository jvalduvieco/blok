package com.blok.handlers

import com.blok.Answer
import com.blok.CommandRequestHandler
import com.blok.common.Bus.CommandBus
import com.blok.handlers.RequestPayload.EditPost
import com.blok.model.PostId
import com.blok.post.EditPostCommand

class PostsEditController(bus: CommandBus) : CommandRequestHandler<EditPost>(EditPost::class.java, bus) {

    override fun processImpl(value: EditPost, urlParams: Map<String, String>, shouldReturnHtml: Boolean): Answer {
        val postId = PostId(urlParams[":post_id"] ?: throw IllegalArgumentException())
        bus.execute(EditPostCommand(postId, value.title, value.content, value.categories))

        return Answer.ok()
    }
}
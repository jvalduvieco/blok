package com.blok.handlers

import com.blok.Answer
import com.blok.CommandRequestHandler
import com.blok.Common.Infrastructure.toJson
import com.blok.common.Bus.CommandBus
import com.blok.handlers.RequestPayload.NewPost
import com.blok.model.PostId
import com.blok.post.CreatePostCommand

class PostsCreateController(bus: CommandBus) : CommandRequestHandler<NewPost>(NewPost::class.java, bus) {

    override fun processImpl(value: NewPost, urlParams: Map<String, String>, shouldReturnHtml: Boolean): Answer {
        val postId = PostId()
        bus.execute(CreatePostCommand(postId,
                value.title,
                value.content,
                value.categories))

        return Answer.created(postId.toJson())
    }
}

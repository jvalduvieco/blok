package com.blok.handlers

import com.blok.Answer
import com.blok.Common.Infrastructure.toJson
import com.blok.QueryRequestHandler
import com.blok.common.Bus.QueryBus
import com.blok.handlers.RequestPayload.NoPayload
import com.blok.model.PostId
import com.blok.post.FindPostQuery
import com.blok.post.FindPostQueryResponse

class GetSinglePostController(bus: QueryBus) : QueryRequestHandler<NoPayload>(NoPayload::class.java, bus) {

    override fun processImpl(value: NoPayload, urlParams: Map<String, String>, shouldReturnHtml: Boolean): Answer {
        val postId = PostId(urlParams[":post_id"] ?: throw IllegalArgumentException())
        val response = bus.execute(FindPostQuery(postId)) as FindPostQueryResponse

        return Answer.ok(response.post.toJson())
    }

}

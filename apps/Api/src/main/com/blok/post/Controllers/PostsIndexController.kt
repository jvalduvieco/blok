package com.blok.handlers

import com.blok.Answer
import com.blok.Common.Infrastructure.toJson
import com.blok.QueryRequestHandler
import com.blok.common.Bus.QueryBus
import com.blok.handlers.RequestPayload.NoPayload
import com.blok.post.ListPostsQuery
import com.blok.post.ListPostsQueryResponse


class PostsIndexController(queryBus: QueryBus) : QueryRequestHandler<NoPayload>(NoPayload::class.java, queryBus) {
    override fun processImpl(value: NoPayload, urlParams: Map<String, String>, shouldReturnHtml: Boolean): Answer {
        val response = bus.execute(ListPostsQuery()) as ListPostsQueryResponse

        return Answer.ok(response.posts.toJson())
    }

}

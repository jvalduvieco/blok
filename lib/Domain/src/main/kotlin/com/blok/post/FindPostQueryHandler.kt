package com.blok.post

import com.blok.common.Bus.AbstractQueryHandler

class FindPostQueryHandler(val postService: PostService):
        AbstractQueryHandler<FindPostQuery, FindPostQueryResponse>(FindPostQuery::class)  {
    override fun execute(query: FindPostQuery):FindPostQueryResponse {
        return FindPostQueryResponse(post = postService.getPost(query.postId))
    }
}

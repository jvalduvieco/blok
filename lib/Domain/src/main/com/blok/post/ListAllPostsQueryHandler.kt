package com.blok.post

import com.blok.common.Bus.AbstractQueryHandler

class ListAllPostsQueryHandler(val postService: PostService):
        AbstractQueryHandler<ListPostsQuery, ListPostsQueryResponse>(ListPostsQuery::class)  {
    override fun execute(query: ListPostsQuery):ListPostsQueryResponse {
        return ListPostsQueryResponse(posts = postService.listAllPosts())
    }
}

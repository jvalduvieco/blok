package com.blok.post

import com.blok.common.Bus.AbstractQueryHandler

class ListAllCommentsQueryHandler(val postService: PostService):
        AbstractQueryHandler<ListAllCommentsQuery, ListAllCommentsQueryResponse>(ListAllCommentsQuery::class)  {
    override fun execute(query: ListAllCommentsQuery):ListAllCommentsQueryResponse {
        return ListAllCommentsQueryResponse(comments = postService.listAllComments(query.postId))
    }
}
package com.blok.post

import com.blok.common.Query
import com.blok.common.Response
import com.blok.model.Comment
import com.blok.model.PostId

class ListAllCommentsQuery (val postId: PostId): Query()
class ListAllCommentsQueryResponse(val comments: List<Comment>): Response()
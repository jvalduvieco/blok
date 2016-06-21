package com.blok.post

import com.blok.common.Query
import com.blok.common.Response
import com.blok.model.Post
import com.blok.model.PostId

class FindPostQuery(val postId: PostId): Query()
class FindPostQueryResponse(val post: Post): Response()
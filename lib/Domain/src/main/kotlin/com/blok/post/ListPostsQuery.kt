package com.blok.post

import com.blok.common.Query
import com.blok.common.Response
import com.blok.model.Post

class ListPostsQuery: Query()

class ListPostsQueryResponse (val posts: List<Post>): Response()
package com.blok.handlers.RequestPayload

import com.blok.model.PostId

data class EditPost(
        val postId: PostId,
        val title: String,
        val content: String,
        val categories: List<String>
)

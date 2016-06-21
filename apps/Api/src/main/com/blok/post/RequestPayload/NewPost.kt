package com.blok.handlers.RequestPayload

class NewPost(
        val title: String,
        val content: String,
        var categories : List<String> = listOf<String>()
)

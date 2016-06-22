package com.blok.model

import com.blok.common.AbstractIdentifier
import java.time.LocalDateTime
import java.util.*

data class Comment (
        val id: CommentId,
        val post_id: PostId,
        var author: String,
        var content: String,
        val submissionDate: LocalDateTime,
        var approved: Boolean = false)

class CommentId(id: UUID = UUID.randomUUID()) : AbstractIdentifier(id){
    constructor(anId: String?): this(UUID.fromString(anId))
}
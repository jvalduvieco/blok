package com.blok.post

import com.blok.common.Command
import com.blok.model.CommentId
import com.blok.model.PostId

class AddCommentCommand(val postId: PostId, val commentId: CommentId, val content: String, val author: String): Command()
package com.blok.post

import com.blok.common.Command
import com.blok.model.CommentId

class DeleteCommentCommand (val commentId: CommentId): Command()
package com.blok.post

import com.blok.common.Command
import com.blok.model.PostId

class EditPostCommand (val postId: PostId,
                       val title: String,
                       val content: String,
                       val categories: List<String>): Command()
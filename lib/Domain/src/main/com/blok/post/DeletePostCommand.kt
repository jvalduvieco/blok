package com.blok.post

import com.blok.common.Command
import com.blok.model.PostId

class DeletePostCommand(val postId: PostId): Command()
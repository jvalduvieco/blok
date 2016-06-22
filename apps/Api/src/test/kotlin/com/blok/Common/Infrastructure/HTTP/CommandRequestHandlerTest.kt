package com.blok.Common.Infrastructure.HTTP

import com.blok.Answer
import com.blok.CommandRequestHandler
import com.blok.common.Bus.CommandBus
import com.blok.common.Bus.SynchronousCommandBus
import com.blok.handlers.RequestPayload.NoPayload
import com.blok.model.PostId
import com.blok.model.PostRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class OkController(bus: CommandBus) : CommandRequestHandler<NoPayload>(NoPayload::class.java, bus) {
    override fun processImpl(value: NoPayload, urlParams: Map<String, String>, shouldReturnHtml: Boolean): Answer {
        return Answer.ok()
    }
}

class PostNotFoundController(bus: CommandBus) : CommandRequestHandler<NoPayload>(NoPayload::class.java, bus) {
    override fun processImpl(value: NoPayload, urlParams: Map<String, String>, shouldReturnHtml: Boolean): Answer {
        throw PostRepository.PostNotFound(PostId())
        return Answer.ok()
    }
}

class CommandRequestHandlerTest {
    private val commandBus: CommandBus = SynchronousCommandBus()
    @Test
    fun requestHandlerReturnsAnswer() {
        val controller = OkController(commandBus)
        val answer = controller.process(NoPayload(), hashMapOf(), false)
        assertEquals(200, answer.code)
    }
    @Test
    fun NotFoundExceptionAnswers404() {
        val controller = PostNotFoundController(commandBus)
        val answer = controller.process(NoPayload(), hashMapOf(), false)
        assertEquals(404, answer.code)
    }
}

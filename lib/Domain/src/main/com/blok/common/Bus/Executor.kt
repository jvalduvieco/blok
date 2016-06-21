package com.blok.common.Bus

import com.blok.common.Response

interface Executor<O> {
    fun registerHandler(handler: Requestable)
    fun execute(input: Input): O
}

interface CommandBus : Executor<Unit>
interface QueryBus : Executor<Response>

class NoHandlerFound(type: String?) : Throwable()
class ClassWithoutSimpleName : Throwable()
class RequestHandlerAlreadyDefined(requestType: String) : Throwable()
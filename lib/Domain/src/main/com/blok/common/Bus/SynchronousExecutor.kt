package com.blok.common.Bus

import com.blok.common.Response
import java.util.*
import java.util.logging.Logger

abstract class SynchronousExecutor<O> : Executor<O> {
    private val logger = Logger.getLogger(this.javaClass.canonicalName)
    val handlers: HashMap<String, Requestable> = hashMapOf()
    override fun registerHandler(handler: Requestable) {
        logger.finer("Registering: ${handler.requestType()}")
        val requestType = handler.requestType()
        if (handlers.contains(requestType)) throw RequestHandlerAlreadyDefined(requestType)
        handlers.put(requestType, handler)
    }

    override fun execute(input: Input): O {
        val type: String? = input.javaClass.kotlin.simpleName
        logger.finer("Looking for  ${type}")
        val handler: Requestable = handlers.get(type) ?: throw NoHandlerFound(type)
        var result = handler.doRequest(input)
        return if (result == MyUnit) Unit as O else result as O
    }
}

class SynchronousCommandBus : SynchronousExecutor<Unit>(), CommandBus
class SynchronousQueryBus : SynchronousExecutor<Response>(), QueryBus

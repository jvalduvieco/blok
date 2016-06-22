package com.blok.common.Bus

import com.blok.common.Command
import com.blok.common.Query
import com.blok.common.Response
import kotlin.reflect.KClass

interface Handler<I : Input, O> {
    fun execute(input: I): O
}

interface Requestable {
    fun requestType(): String
    fun doRequest(input: Input): Output
}

interface Input
interface Output
object MyUnit : Output

abstract class AbstractRequestHandler<I : Input, O>(private val valueClass: KClass<I>) : Handler<I, O>, Requestable {
    override fun requestType(): String {
        return valueClass.simpleName ?: throw ClassWithoutSimpleName()
    }

    override fun doRequest(input: Input): Output {
        val inputTyped: I = input as I
        val result: Output = execute(inputTyped) as? Output ?: MyUnit
        return result
    }
}

abstract class AbstractCommandHandler<C : Command>(private val commandClass: KClass<C>) : AbstractRequestHandler<C, Unit>(commandClass)
abstract class AbstractQueryHandler<C : Query, R : Response>(private val queryClass: KClass<C>) : AbstractRequestHandler<C, R>(queryClass)
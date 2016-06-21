package com.blok

import com.blok.Common.Infrastructure.HTTP.ErrorPayload
import com.blok.Common.Infrastructure.toJson
import com.blok.common.Bus.CommandBus
import com.blok.common.Bus.QueryBus
import com.blok.handlers.RequestPayload.NoPayload
import com.blok.model.AbstractRepository
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.apache.http.HttpStatus
import spark.Request
import spark.Response
import spark.Route
import java.util.logging.Logger

abstract class BusRequestHandler<V : Any, B: Any>(private val valueClass: Class<V>, open protected val bus: B) : RequestHandler<V>, Route {
    private val logger = Logger.getLogger(this.javaClass.canonicalName)
    override fun process(value: V, urlParams: Map<String, String>, shouldReturnHtml: Boolean): Answer {
        return try {
            processImpl(value, urlParams, shouldReturnHtml)
        } catch (e: IllegalArgumentException) {
            Answer.badRequest(ErrorPayload("Exception: ${e.javaClass.canonicalName}: ${e.message}").toJson())
        } catch (e: AbstractRepository.NotFound) {
            Answer.notFound(ErrorPayload("Exception: ${e.javaClass.canonicalName}: ${e.message}").toJson())
        }
    }

    protected abstract fun processImpl(value: V, urlParams: Map<String, String>, shouldReturnHtml: Boolean): Answer


    @Throws(Exception::class)
    override fun handle(request: Request, response: Response): String? {
        try {
            var value: V = castPayload(request)
            val urlParams = request.params()

            val answer = process(value, urlParams, shouldReturnHtml(request))

            response.status(answer.code)
            response.type(getRequestType(request))
            response.body(answer.body)

            return answer.body?: ""

        } catch (e: JsonSyntaxException) {
            val payload = ErrorPayload( "Bad request: [${request.url()}]" +
                    "[${request.body()}]" +
                    "[Message: ${e.message}]" +
                    "[${e.printStackTrace()}]")
            logger.finer(payload.message)
            response.status(HttpStatus.SC_BAD_REQUEST)
            response.body(payload.toJson())
            return payload.toJson()
        } catch (e: Throwable) {
            val payload = ErrorPayload("Internal server error: [${request.url()}]" +
                    "[${request.body()}]" +
                    "[Message: ${e.message}]" +
                    "[${e.printStackTrace()}]")
            logger.severe(payload.message)
            response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
            response.body(payload.toJson())
            return payload.toJson()
        }
    }

    private fun getRequestType(request: Request): String {
        return if (shouldReturnHtml(request)) {
            "text/html"
        } else {
            "application/json"
        }
    }

    private fun castPayload(request: Request): V {
        return if (valueClass != NoPayload::class.java) {
            Gson().fromJson(request.body(), valueClass)
        } else NoPayload() as V
    }

    companion object {
        private fun shouldReturnHtml(request: Request): Boolean {
            val accept = request.headers("Accept")
            return accept != null && accept.contains("text/html")
        }
    }

}

abstract class CommandRequestHandler<V : Any>(valueClass: Class<V>, override val bus: CommandBus): BusRequestHandler<V, CommandBus>(valueClass, bus)
abstract class QueryRequestHandler<V : Any>(valueClass: Class<V>, override val bus: QueryBus): BusRequestHandler<V, QueryBus>(valueClass, bus)
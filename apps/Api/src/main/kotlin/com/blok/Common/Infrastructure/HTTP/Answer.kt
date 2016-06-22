package com.blok

import org.apache.http.HttpStatus

data class Answer (val code: Int, val body: String? = ""){
    companion object {
        fun ok(body: String? = ""): Answer {
            return Answer(HttpStatus.SC_OK, body)
        }
        fun created(body: String): Answer {
            return Answer(HttpStatus.SC_CREATED, body)
        }
        fun badRequest(body: String): Answer {
            return Answer(HttpStatus.SC_BAD_REQUEST, body)
        }
        fun notFound(body: String): Answer {
            return Answer(HttpStatus.SC_NOT_FOUND, body)
        }
    }
}

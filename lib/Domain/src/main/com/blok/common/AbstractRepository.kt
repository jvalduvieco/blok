package com.blok.model

import com.blok.common.AbstractIdentifier

interface AbstractRepository {
    open class NotFound(id: AbstractIdentifier): Throwable(id.toString())
}
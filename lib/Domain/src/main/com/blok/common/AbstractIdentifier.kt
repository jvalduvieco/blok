package com.blok.common

import java.io.Serializable
import java.util.*

open class AbstractIdentifier(private val id: UUID = UUID.randomUUID()) : Serializable {
    constructor(anId: String?) : this(UUID.fromString(anId))
    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return this.id.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as AbstractIdentifier
        if (id == other.id) return true
        return false
    }
    // FIXME Have doubts here. Should we return a neutral type like String? This is exposing internals and smells...
    operator fun invoke(): UUID = id
}
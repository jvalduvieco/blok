package com.blok.Common.Infrastructure

import com.google.gson.Gson

fun Any.toJson(): String {
        return Gson().toJson(this)
}
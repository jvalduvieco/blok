package com.blok.common

data class DatabaseSetup (
        val name: String = "blog_post",
        val host: String = "localhost",
        val username: String = "blog_owner",
        val password: String = "sparkforthewin",
        val port: Int = 5432
)
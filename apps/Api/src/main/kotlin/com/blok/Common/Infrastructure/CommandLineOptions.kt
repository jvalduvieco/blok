package com.blok

import com.beust.jcommander.Parameter

internal class CommandLineOptions {

    @Parameter(names = arrayOf("--debug"))
    var debug = false

    @Parameter(names = arrayOf("--service-port"))
    var servicePort: Int? = 4567

    @Parameter(names = arrayOf("--database"))
    var database = "blog_post"

    @Parameter(names = arrayOf("--db-host"))
    var dbHost = "localhost"

    @Parameter(names = arrayOf("--db-username"))
    var dbUsername = "blog_owner"

    @Parameter(names = arrayOf("--db-password"))
    var dbPassword = "sparkforthewin"

    @Parameter(names = arrayOf("--db-port"))
    var dbPort: Int = 5432
}

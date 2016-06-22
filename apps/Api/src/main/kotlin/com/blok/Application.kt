package com.blok

import com.beust.jcommander.JCommander
import com.blok.common.Bus.CommandBus
import com.blok.common.Bus.QueryBus
import com.blok.common.Bus.SynchronousCommandBus
import com.blok.common.Bus.SynchronousQueryBus
import com.blok.common.DatabaseSetup
import com.blok.handlers.*
import com.blok.model.PostRepository
import com.blok.post.*
import com.blok.post.Infrastructure.PostgresPostRepository
import spark.Spark
import spark.route.RouteOverview
import java.util.logging.Level
import java.util.logging.Logger

class Application {
    private val logger = Logger.getLogger(Bootstrap::class.java.canonicalName)

    fun start(args: Array<String>) {
        val options = CommandLineOptions()
        JCommander(options, *args)

        logger.info("Options.debug = " + options.debug)
        logger.info("Options.database = " + options.database)
        logger.info("Options.dbHost = " + options.dbHost)
        logger.info("Options.dbUsername = " + options.dbUsername)
        logger.info("Options.dbPort = " + options.dbPort)
        logger.info("Options.servicePort = " + options.servicePort)
        logger.finer("DEBUG enabled")

        setupLogger(options)
        setUpWebserver(options)
        val postRepository = setupPostRepository(options)
        val commandBus: CommandBus = SynchronousCommandBus()
        val postService: PostService = PostService(postRepository)
        commandBus.registerHandler(CreatePostCommandHandler(postService))
        commandBus.registerHandler(EditPostCommandHandler(postService))
        commandBus.registerHandler(DeletePostCommandHandler(postService))
        commandBus.registerHandler(AddCommentCommandHandler(postService))
        commandBus.registerHandler(DeleteCommentCommandHandler(postService))
        val queryBus: QueryBus = SynchronousQueryBus()
        queryBus.registerHandler(ListAllPostsQueryHandler(postService))
        queryBus.registerHandler(FindPostQueryHandler(postService))
        queryBus.registerHandler(ListAllCommentsQueryHandler(postService))

        setupPostRoutes(postRepository, commandBus, queryBus)
    }

    private fun setupPostRepository(options: CommandLineOptions): PostRepository {
        return PostgresPostRepository(
                DatabaseSetup(
                        host = options.dbHost,
                        username = options.dbUsername,
                        password = options.dbPassword,
                        port = options.dbPort
                )
        )
    }
    private fun setupLogger(options: CommandLineOptions) {
        logger.level = if (options.debug) {
            Level.ALL
        } else {
            Level.INFO
        }
    }
    private fun setUpWebserver(options: CommandLineOptions) {
        Spark.port(options.servicePort ?: 8080)
    }
    private fun setupPostRoutes(repository: PostRepository, commandBus: CommandBus, queryBus: QueryBus) {
        RouteOverview.enableRouteOverview()
        Spark.post("/posts", PostsCreateController(commandBus))
        Spark.get("/posts", PostsIndexController(queryBus))
        Spark.get("/posts/:post_id", GetSinglePostController(queryBus))
        Spark.put("/posts/:post_id", PostsEditController(commandBus))
        Spark.delete("/posts/:post_id", PostsDeleteController(commandBus))
        Spark.post("/posts/:post_id/comments", CommentsCreateController(commandBus))
        Spark.get("/posts/:post_id/comments", CommentsListController(queryBus))
        Spark.delete("/posts/:post_id/comments/:comment_id", CommentDeleteController(commandBus))
        Spark.get("/alive") { request, response -> "ok" }
    }
}
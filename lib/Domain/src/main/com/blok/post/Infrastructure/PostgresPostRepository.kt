package com.blok.post.Infrastructure

import com.blok.common.DatabaseSetup
import com.blok.model.*
import com.github.andrewoma.kwery.core.DefaultSession
import com.github.andrewoma.kwery.core.Row
import com.github.andrewoma.kwery.core.dialect.PostgresDialect
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Timestamp

fun Post.toMap(): Map<String, Any?> = mapOf(
        "id" to this.id(),
        "title" to this.title,
        "content" to this.content,
        "publishing_date" to Timestamp.valueOf(this.publishingDate)
)

fun Comment.toMap(): Map<String, Any?> = mapOf(
        "id" to this.id(),
        "content" to this.content,
        "author" to this.author,
        "post_id" to this.post_id(),
        "approved" to this.approved,
        "submission_date" to Timestamp.valueOf(this.submissionDate)
)
open class PostgresPostRepository(dbSetup: DatabaseSetup) : PostRepository {
    val postMapper: (Row) -> Post = { row ->
        Post(PostId(row.string("id")),
                row.string("title"),
                row.string("content"),
                row.timestamp("publishing_date").toLocalDateTime(),
                row.stringOrNull("categories")?.split(',')?:listOf()
        )
    }
    val commentMapper: (Row) -> Comment = {row ->
        Comment(CommentId(row.string("id")),
                PostId(row.string("post_id")),
                row.string("author"),
                row.string("content"),
                row.timestamp("submission_date").toLocalDateTime(),
                row.boolean("approved")
        )
    }
    protected val session: DefaultSession
    init {
        Class.forName( "org.postgresql.Driver" )
        val connection: Connection = DriverManager.getConnection(
                "jdbc:postgresql://${dbSetup.host}:${dbSetup.port}/${dbSetup.name}?", dbSetup.username, dbSetup.password)
        session = DefaultSession(connection, PostgresDialect())
    }
    override fun save(post: Post): PostId {
        session.transaction {
            val sql = "INSERT INTO " +
                    "posts (id, title, content, publishing_date) " +
                    "VALUES (:id, :title, :content, :publishing_date) ON CONFLICT (id) " +
                    "DO UPDATE SET (title, content, publishing_date) = (:title, :content, :publishing_date) " +
                    "WHERE posts.id = :id"
            session.update(sql, post.toMap())
            session.update("DELETE FROM posts_categories WHERE id = :id", mapOf("id" to post.id()))
            val sqlCategories = "INSERT INTO " +
                    "posts_categories(id, category) " +
                    "VALUES (:id, :category)"
            post.categories.forEach {
                category ->
                session.update(sqlCategories, mapOf("id" to post.id(), "category" to category))
            }
        }
        return post.id
    }
    override fun save(comment: Comment): CommentId {
        val sql = "INSERT INTO " +
                "comments " +
                "(id, post_id, author, content, approved, submission_date) " +
                "VALUES " +
                "(:id, :post_id, :author, :content, :approved, :submission_date)"
        session.update(sql, comment.toMap())
        return comment.id
    }
    override fun update(post: Post) {
        save(post)
    }
    override fun listAllPosts(): List<Post> {
        return session.select(
                "SELECT p.id, p.title, p.content, p.publishing_date,string_agg(pc.category,',') AS categories " +
                        "FROM posts AS p LEFT JOIN posts_categories AS pc ON p.id = pc.id " +
                        "GROUP BY p.id", mapper = postMapper)
    }
    override fun getAllCommentsOn(id: PostId): List<Comment> {
        if (!existPost(id)) throw PostRepository.PostNotFound(id)
        return session.select(
                "SELECT * " +
                        "FROM comments " +
                        "WHERE post_id=:post_id",
                hashMapOf("post_id" to id()),
                mapper = commentMapper
        )
    }
    override fun existPost(id: PostId): Boolean {
        return session.select("SELECT " +
                "FROM posts " +
                "WHERE id=:post_id",
                hashMapOf("post_id" to id()),
                mapper = {}
        ).count() == 1
    }
    override fun getPost(id: PostId): Post {
        return session.select(
                "SELECT p.id, p.title, p.content, p.publishing_date,string_agg(pc.category,',') AS categories " +
                "FROM posts AS p LEFT JOIN posts_categories AS pc ON p.id = pc.id " +
                "WHERE p.id=:post_id " +
                "GROUP BY p.id",
                hashMapOf("post_id" to id()),
                mapper = postMapper
        ).firstOrNull()?: throw PostRepository.PostNotFound(id)
    }
    override fun deletePost(id: PostId) {
        if (!existPost(id)) throw PostRepository.PostNotFound(id)
        session.update("DELETE FROM posts WHERE id=:post_id", hashMapOf("post_id" to id()))
    }
    override fun deleteComment(id: CommentId) {
        session.update("DELETE FROM comments WHERE id=:comment_id", hashMapOf("comment_id" to id()))
    }
}

class PostgresPostRepositoryForTesting(dbSetup: DatabaseSetup) : PostgresPostRepository(dbSetup), PostRepositoryForTesting, PostRepository {
    override fun cleanDatabase() {
        session.update("TRUNCATE posts CASCADE")
    }
}
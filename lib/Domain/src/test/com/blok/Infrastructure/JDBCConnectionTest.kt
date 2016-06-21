package test.com.blok.Infrastructure

import com.github.andrewoma.kwery.core.DefaultSession
import com.github.andrewoma.kwery.core.Row
import com.github.andrewoma.kwery.core.dialect.PostgresDialect
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Timestamp

class JDBCConnectionTest {
    val session: DefaultSession
    init {
        Class.forName( "org.postgresql.Driver" )
        val connection: Connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/blog?","blog_owner","sparkforthewin" )
        session = DefaultSession(connection, PostgresDialect())
    }
    data class Actor(val firstName: String, val lastName: String?, val id: Int = 0, val lastUpdate: Timestamp = Timestamp(System.currentTimeMillis()))
    fun Actor.toMap(): Map<String, Any?> = mapOf(
            "first_name" to this.firstName,
            "last_name" to this.lastName,
            "last_update" to this.lastUpdate,
            "actor_id" to this.id
    )
    val actorMapper: (Row) -> Actor = { row ->
        Actor(row.string("first_name"), row.stringOrNull("last_name"), row.int("actor_id"), row.timestamp("last_update"))
    }
    private fun insertAnActor(): Int {
        val actor: Actor = Actor("John", "Doe", 1)
        val sql = "insert into actor(actor_id, first_name, last_name, last_update) values (:actor_id, :first_name, :last_name, :last_update)"
        return session.update(sql, actor.toMap())
    }
    @Before
    fun createTables() {
        val sql = """
            create table actor (
                actor_id    serial,
                first_name  character varying(45) not null,
                last_name   character varying(45) null,
                last_update timestamp             not null
            )
        """
        session.update(sql)

    }
    @Test
    fun iCanConnectToPostgresUsingJDBC() {
        Class.forName( "org.postgresql.Driver" )
        val connection: Connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/blog?","blog_owner","sparkforthewin" )
        val session = DefaultSession(connection, PostgresDialect())
    }

    @Test
    fun iCanInsertActors() {
        val count = insertAnActor()
        assertEquals(1, count)
    }

    @Test
    fun iCanSelectActors() {
        insertAnActor()
        val sql = "select actor_id, first_name, last_name, last_update from actor"
        val result = session.select(sql,  mapper = actorMapper)
        assertEquals(1,result.count())
    }
    @Test
    fun iCanSelectActorsWithParams() {
        insertAnActor()
        val params = hashMapOf<String, Any?>()
        params["ids"] = 1
        val sql = "select actor_id, first_name, last_name, last_update from actor where actor_id in (:ids) order by actor_id"
        val result = session.select(sql, params, mapper = actorMapper)
        assertEquals(1,result.count())
        assertEquals("John", result.first().firstName)

    }
    @Test
    fun iCanDeleteActors() {
        insertAnActor()
        val count = session.update("delete from actor where actor_id = :id", mapOf("id" to 1))
        assertEquals(1, count)
    }
    @Test
    fun iCanDeleteActorsThatDoNotExist() {
        val count = session.update("delete from actor where actor_id = :id", mapOf("id" to 1))
        assertEquals(0, count)
    }
    @After
    fun cleanTables() {
        val sql = """
            DROP TABLE actor
        """
        session.update(sql)
    }
}
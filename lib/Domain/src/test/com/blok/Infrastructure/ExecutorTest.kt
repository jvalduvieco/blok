package com.blok.Infrastructure

import com.blok.common.Command
import com.blok.common.Bus.*
import com.blok.common.Query
import com.blok.common.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RegisterAUser(val name: String, val email: String) : Command()
class sayWTF(val name: String) : Command()
class UnhandledCommand() : Command()

class RegisterAUserUseCase : AbstractCommandHandler<RegisterAUser>(RegisterAUser::class) {
    override fun execute(command: RegisterAUser) {
        println("YEEEHAAAAH ${command.name} with email ${command.email} has been registered!")
    }
}

class WTFUseCase : AbstractCommandHandler<sayWTF>(sayWTF::class) {
    var hasBeenCalled: Boolean = false
    override fun execute(command: sayWTF) {
        println("WTF ${command.name}!")
        hasBeenCalled = true
    }
}

class ListUsersWithAName(val name: String) : Query()
class ListOperations(val name: String) : Query()
class ListUsersWithANameResponse(val users: List<String>) : Response()
class ListOperationsResponse(val operations: List<String>) : Response()
class UnhandledQuery() : Query()

class ListUsersWithANameUseCase : AbstractQueryHandler<ListUsersWithAName, ListUsersWithANameResponse>(ListUsersWithAName::class) {
    override fun execute(command: ListUsersWithAName): ListUsersWithANameResponse {
        return ListUsersWithANameResponse(listOf("user1", "user2", "user3"))
    }
}

class ListOperationsUseCase : AbstractQueryHandler<ListOperations, ListOperationsResponse>(ListOperations::class) {
    override fun execute(command: ListOperations): ListOperationsResponse {
        return ListOperationsResponse(listOf("operation1", "operation2", "operation3"))
    }
}

class ExecutorTest {
    @Test
    fun iCanSendACommand() {
        val commandBus: CommandBus = SynchronousCommandBus()
        val wtfUseCase = WTFUseCase()
        commandBus.registerHandler(RegisterAUserUseCase())
        commandBus.registerHandler(wtfUseCase)

        assertEquals(Unit, commandBus.execute(RegisterAUser("user123", "test@qq.com")))
        commandBus.execute(sayWTF("now"))
        assertTrue(wtfUseCase.hasBeenCalled)
    }

    @Test
    fun iCanSendAQuery() {
        val queryBus: QueryBus = SynchronousQueryBus()
        queryBus.registerHandler(ListUsersWithANameUseCase())
        queryBus.registerHandler(ListOperationsUseCase())

        val response1: ListUsersWithANameResponse = queryBus.execute(ListUsersWithAName("user")) as ListUsersWithANameResponse
        assertEquals(listOf("user1", "user2", "user3"), response1.users)
        val response2: ListOperationsResponse = queryBus.execute(ListOperations("user1")) as ListOperationsResponse
        assertEquals(listOf("operation1", "operation2", "operation3"), response2.operations)
    }

    @Test(expected = NoHandlerFound::class)
    fun unhandledCommandThrowsException() {
        val commandBus: CommandBus = SynchronousCommandBus()
        commandBus.execute(UnhandledCommand())
    }

    @Test(expected = NoHandlerFound::class)
    fun unhandledQueryThrowsException() {
        val queryBus: QueryBus = SynchronousQueryBus()
        queryBus.execute(UnhandledQuery())
    }

    @Test(expected = RequestHandlerAlreadyDefined::class)
    fun registeringAHandlerTwiceThrowsException() {
        val queryBus: QueryBus = SynchronousQueryBus()
        queryBus.registerHandler(ListUsersWithANameUseCase())
        queryBus.registerHandler(ListUsersWithANameUseCase())
    }
}
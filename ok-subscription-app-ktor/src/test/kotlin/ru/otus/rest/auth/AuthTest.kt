package ru.otus.rest.auth

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.otus.constants.TEST_DEBUG
import ru.otus.constants.getPlanCreateReq
import ru.otus.initInMemoryApp
import ru.otus.myRestClient
import ru.otus.settings.KtorAuthConfig
import kotlin.test.assertEquals

class AuthTest {

    @Test
    fun invalidAudience() = testApplication {
        initInMemoryApp()
        val client = myRestClient()
        val response = client.post("/plan/create") {
            contentType(ContentType.Application.Json)
            setBody(getPlanCreateReq(TEST_DEBUG))
            addAuth(
                id = "123", groups = listOf("ADMIN"), config = KtorAuthConfig.TEST.copy(audience = "invalid")
            )
        }
        assertEquals(401, response.status.value)
    }

    @Test
    fun invalidSecret() = testApplication {
        initInMemoryApp()
        val client = myRestClient()
        val response = client.post("/plan/create") {
            contentType(ContentType.Application.Json)
            setBody(getPlanCreateReq(TEST_DEBUG))
            addAuth(
                id = "123", groups = listOf("ADMIN"), config = KtorAuthConfig.TEST.copy(secret = "invalid")
            )
        }
        assertEquals(401, response.status.value)
    }

    @Test
    fun validAuth() = testApplication {
        initInMemoryApp()
        val client = myRestClient()
        val response = client.post("/plan/create") {
            contentType(ContentType.Application.Json)
            setBody(getPlanCreateReq(TEST_DEBUG))
            addAuth(
                id = "123", groups = listOf("ADMIN"), config = KtorAuthConfig.TEST
            )
        }
        assertEquals(200, response.status.value)
    }
}
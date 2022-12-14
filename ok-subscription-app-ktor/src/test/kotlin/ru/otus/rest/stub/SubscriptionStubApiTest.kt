package ru.otus.rest.stub

import ru.otus.constants.STUB_DEBUG
import SubscriptionStubs
import ru.otus.constants.getSubscriptionPayReq
import ru.otus.constants.getSubscriptionReadReq
import ru.otus.constants.getSubscriptionSearchReq
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.otus.initStubApp
import ru.otus.myRestClient
import ru.otus.rest.auth.addAuth
import ru.otus.settings.KtorAuthConfig
import ru.otuskotlin.subscription.api.v1.models.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SubscriptionStubApiTest {

    @Test
    fun `read route`() = testApplication {
        initStubApp()
        val client = myRestClient()

        val response = client.post("/subscription/read") {
            contentType(ContentType.Application.Json)
            setBody(getSubscriptionReadReq(debug = STUB_DEBUG))
            addAuth(
                id = "123", groups = listOf("ADMIN"), config = KtorAuthConfig.TEST
            )
        }
        val responseBody = response.body<SubscriptionReadResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals(getSubscriptionReadReq(debug = STUB_DEBUG).subscription?.id, responseBody.subscription?.id)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)
    }

    @Test
    fun `search route`() = testApplication {
        initStubApp()
        val client = myRestClient()

        val response = client.post("/subscription/search") {
            contentType(ContentType.Application.Json)
            setBody(getSubscriptionSearchReq(debug = STUB_DEBUG))
            addAuth(
                id = "123", groups = listOf("ADMIN"), config = KtorAuthConfig.TEST
            )
        }
        val responseBody = response.body<SubscriptionSearchResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals(SubscriptionStubs.SUBSCRIPTION1.id.asString(), responseBody.subscriptions?.get(0)?.id)
        assertTrue(responseBody.subscriptions?.size?.let { it > 1 } ?: false)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)
    }
    
    @Test
    fun `pay route`() = testApplication {
        initStubApp()
        val client = myRestClient()

        val response = client.post("/subscription/pay") {
            contentType(ContentType.Application.Json)
            setBody(getSubscriptionPayReq(debug = STUB_DEBUG))
            addAuth(
                id = "123", groups = listOf("ADMIN"), config = KtorAuthConfig.TEST
            )
        }

        val responseBody = response.body<SubscriptionPayResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals(getSubscriptionPayReq(debug = STUB_DEBUG).subscription?.id, responseBody.subscription?.id)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)
    }
}
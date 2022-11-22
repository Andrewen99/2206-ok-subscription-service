package ru.otus.rest

import SubscriptionStubs
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.otus.DEBUG
import ru.otus.ReqConstants.SUBSCRIPTION_PAY_REQ
import ru.otus.ReqConstants.SUBSCRIPTION_READ_REQ
import ru.otus.ReqConstants.SUBSCRIPTION_SEARCH_REQ
import ru.otus.myRestClient
import ru.otuskotlin.subscription.api.v1.models.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SubscriptionRouteTest {

    @Test
    fun `read route`() = testApplication {
        val client = myRestClient()

        val response = client.post("/subscription/read") {
            contentType(ContentType.Application.Json)
            setBody(SUBSCRIPTION_READ_REQ)
        }
        val responseBody = response.body<SubscriptionReadResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals(SUBSCRIPTION_READ_REQ.subscription?.id, responseBody.subscription?.id)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)
    }

    @Test
    fun `search route`() = testApplication {
        val client = myRestClient()

        val response = client.post("/subscription/search") {
            contentType(ContentType.Application.Json)
            setBody(SUBSCRIPTION_SEARCH_REQ)
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
        val client = myRestClient()

        val response = client.post("/subscription/pay") {
            contentType(ContentType.Application.Json)
            setBody(SUBSCRIPTION_PAY_REQ)
        }

        val responseBody = response.body<SubscriptionPayResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals(SUBSCRIPTION_PAY_REQ.subscription?.id, responseBody.subscription?.id)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)
    }
}
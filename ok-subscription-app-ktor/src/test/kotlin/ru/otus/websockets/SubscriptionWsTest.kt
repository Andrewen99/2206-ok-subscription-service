package ru.otus.websockets

import org.junit.Test
import ru.otus.ReqConstants
import ru.otuskotlin.subscription.api.v1.models.ResponseResult
import ru.otuskotlin.subscription.api.v1.models.SubscriptionPayResponse
import ru.otuskotlin.subscription.api.v1.models.SubscriptionReadResponse
import ru.otuskotlin.subscription.api.v1.models.SubscriptionSearchResponse
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SubscriptionWsTest {

    @Test
    fun read() = wsTest<SubscriptionReadResponse>(
        "/subscription",
        ReqConstants.SUBSCRIPTION_READ_REQ
    ) { response ->
        println("\n\n$response\n\n")
        assertEquals(ReqConstants.SUBSCRIPTION_READ_REQ.subscription?.id, response.subscription?.id)
        assertEquals("req123", response.requestId)
        assertEquals(ResponseResult.SUCCESS, response.result)
    }

    @Test
    fun search() = wsTest<SubscriptionSearchResponse>(
        "/subscription",
        ReqConstants.SUBSCRIPTION_SEARCH_REQ
    ) { response ->
        println("\n\n$response\n\n")
        assertEquals(SubscriptionStubs.SUBSCRIPTION1.id.asString(), response.subscriptions?.get(0)?.id)
        assertTrue(response.subscriptions?.size?.let { it > 1 } ?: false)
        assertEquals("req123", response.requestId)
        assertEquals(ResponseResult.SUCCESS, response.result)
    }

    @Test
    fun pay() = wsTest<SubscriptionPayResponse>(
        "/subscription",
        ReqConstants.SUBSCRIPTION_PAY_REQ
    ) { response ->
        println("\n\n$response\n\n")
        assertEquals(ReqConstants.SUBSCRIPTION_PAY_REQ.subscription?.id, response.subscription?.id)
        assertEquals("req123", response.requestId)
        assertEquals(ResponseResult.SUCCESS, response.result)
    }
}
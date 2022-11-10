package ru.otus.websockets.stub

import ru.otus.constants.STUB_DEBUG
import ru.otus.constants.getSubscriptionPayReq
import ru.otus.constants.getSubscriptionReadReq
import ru.otus.constants.getSubscriptionSearchReq
import org.junit.Test
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
        getSubscriptionReadReq()
    ) { response ->
        println("\n\n$response\n\n")
        assertEquals(getSubscriptionReadReq().subscription?.id, response.subscription?.id)
        assertEquals("req123", response.requestId)
        assertEquals(ResponseResult.SUCCESS, response.result)
    }

    @Test
    fun search() = wsTest<SubscriptionSearchResponse>(
        "/subscription",
        getSubscriptionSearchReq(STUB_DEBUG)
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
        getSubscriptionPayReq()
    ) { response ->
        println("\n\n$response\n\n")
        assertEquals(getSubscriptionPayReq().subscription?.id, response.subscription?.id)
        assertEquals("req123", response.requestId)
        assertEquals(ResponseResult.SUCCESS, response.result)
    }
}
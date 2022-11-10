package ru.otus.rest.inmemory

import DATE_FORMATTER
import PlanStubs
import SubscriptionStubs
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.datetime.toJavaLocalDate
import models.plan.Plan
import models.plan.PlanId
import models.plan.PlanLock
import models.subscription.SubscriptionId
import models.subscription.SubscriptionLock
import org.junit.Test
import ru.otus.UUID_OLD
import ru.otus.constants.*
import ru.otus.initInMemoryApp
import ru.otus.module
import ru.otus.myRestClient
import ru.otuskotlin.subscription.api.v1.models.*
import util.getTodayAsLocalDate
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SubscriptionInMemoryApiTest {

    private val initSubscription = SubscriptionStubs.SUBSCRIPTION1
        .copy(id = SubscriptionId(UUID_OLD), planId=PlanId(UUID_OLD), lock = SubscriptionLock(UUID_OLD))
    private val initPlan = PlanStubs.PLAN1.copy(id = PlanId(UUID_OLD), lock = PlanLock(UUID_OLD))

    private val initSubscriptions = listOf(
        initSubscription,
        SubscriptionStubs.SUBSCRIPTION2
    )

    @Test
    fun `read route`() = testApplication {
        initInMemoryApp(initSubscriptions = initSubscriptions)
        val client = myRestClient()

        val subscriptionReq = getSubscriptionReadReq(id = initSubscription.id.asString(), debug = TEST_DEBUG) 
        val response = client.post("/subscription/read") {
            contentType(ContentType.Application.Json)
            setBody(subscriptionReq)
        }
        val responseBody = response.body<SubscriptionReadResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)

        assertEquals(subscriptionReq.subscription?.id, responseBody.subscription?.id)
    }

    @Test
    fun `search route`() = testApplication {
        initInMemoryApp(initSubscriptions = initSubscriptions)
        val client = myRestClient()

        val request = getSubscriptionSearchReq(TEST_DEBUG)
        val response = client.post("/subscription/search") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val responseBody = response.body<SubscriptionSearchResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)

        assertTrue(responseBody.subscriptions?.size?.let { it >= 1 } ?: false)

    }

    @Test
    fun `pay route`() = testApplication {
        initInMemoryApp(
            initPlans = listOf(initPlan),
            initSubscriptions = initSubscriptions
        )
        val client = myRestClient()

        val subscriptionReq = getSubscriptionPayReq(id = initSubscription.id.asString(), debug = TEST_DEBUG) 
        val response = client.post("/subscription/pay") {
            contentType(ContentType.Application.Json)
            setBody(subscriptionReq)
        }

        val responseBody = response.body<SubscriptionPayResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)


        assertEquals(subscriptionReq.subscription?.id, responseBody.subscription?.id)
        assertEquals(DATE_FORMATTER.format(LocalDate.now()), responseBody.subscription?.startDate)
        assertEquals(
            DATE_FORMATTER.format(LocalDate.now().plusMonths(initPlan.duration.toLong())),
            responseBody.subscription?.endDate
        )
        assertEquals(SubscriptionResponseObject.PaymentStatus.PAID, responseBody.subscription?.paymentStatus)
    }



}
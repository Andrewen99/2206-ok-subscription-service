package ru.otus.rest

import DATE_FORMATTER
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import models.plan.Plan
import models.plan.PlanId
import models.plan.PlanLock
import models.subscription.Subscription
import models.subscription.SubscriptionId
import models.subscription.SubscriptionLock
import org.junit.Test
import ru.otus.UUID_OLD
import ru.otus.constants.TEST_DEBUG
import ru.otus.constants.getSubscriptionPayReq
import ru.otus.constants.getSubscriptionReadReq
import ru.otus.constants.getSubscriptionSearchReq
import ru.otus.myRestClient
import ru.otus.rest.auth.addAuth
import ru.otus.settings.KtorAuthConfig
import ru.otuskotlin.subscription.api.v1.models.*
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class SubscriptionApiTest {
    open val initSubscription = SubscriptionStubs.SUBSCRIPTION1
        .copy(id = SubscriptionId(UUID_OLD), planId= PlanId(UUID_OLD), lock = SubscriptionLock(UUID_OLD))
    open val initPlan = PlanStubs.PLAN1.copy(id = PlanId(UUID_OLD), lock = PlanLock(UUID_OLD))

    open val initSubscriptions = listOf(
        initSubscription,
        SubscriptionStubs.SUBSCRIPTION2
    )

    abstract fun TestApplicationBuilder.initApp(initPlans: List<Plan> = emptyList(),
                                                initSubscriptions: List<Subscription> = emptyList())

    @Test
    fun `read route`() = testApplication {
        initApp(initSubscriptions = initSubscriptions)
        val client = myRestClient()

        val subscriptionReq = getSubscriptionReadReq(id = initSubscription.id.asString(), debug = TEST_DEBUG)
        val response = client.post("/subscription/read") {
            contentType(ContentType.Application.Json)
            setBody(subscriptionReq)
            addAuth(
                id = "123", groups = listOf("ADMIN"), config = KtorAuthConfig.TEST
            )
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
        initApp(initSubscriptions = initSubscriptions)
        val client = myRestClient()

        val request = getSubscriptionSearchReq(TEST_DEBUG)
        val response = client.post("/subscription/search") {
            contentType(ContentType.Application.Json)
            setBody(request)
            addAuth(
                id = "123", groups = listOf("ADMIN"), config = KtorAuthConfig.TEST
            )
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
        initApp(
            initPlans = listOf(initPlan),
            initSubscriptions = initSubscriptions
        )
        val client = myRestClient()

        val subscriptionReq = getSubscriptionPayReq(id = initSubscription.id.asString(), debug = TEST_DEBUG)
        val response = client.post("/subscription/pay") {
            contentType(ContentType.Application.Json)
            setBody(subscriptionReq)
            addAuth(
                id = "owner-id-1", groups = listOf("ADMIN"), config = KtorAuthConfig.TEST
            )
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
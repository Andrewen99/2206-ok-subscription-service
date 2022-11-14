package ru.otus.rest.inmemory

import PlanStubs
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import models.plan.PlanId
import models.plan.PlanLock
import models.subscription.SubscriptionId
import models.subscription.SubscriptionLock
import org.junit.Test
import ru.otus.UUID_NEW
import ru.otus.UUID_OLD
import ru.otus.constants.*
import ru.otus.initInMemoryApp
import ru.otus.myRestClient
import ru.otuskotlin.subscription.api.v1.models.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class PlanImMemoryApiTest {

    private val initPlan = PlanStubs.PLAN1.copy(id = PlanId(UUID_OLD), lock = PlanLock(UUID_OLD))

    private val initPlans = listOf(
        initPlan,
        PlanStubs.PLAN2
    )

    @Test
    fun `create route`() = testApplication {
        initInMemoryApp()
        val client = myRestClient()

        val response = client.post("/plan/create") {
            contentType(ContentType.Application.Json)
            setBody(getPlanCreateReq(TEST_DEBUG))
        }
        val responseBody = response.body<PlanCreateResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals(UUID_NEW, responseBody.plan?.id)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)
    }

    @Test
    fun `update route`() = testApplication {
        initInMemoryApp(initPlans = initPlans)
        val planUpdateReq = getPlanUpdateReq(TEST_DEBUG, initPlan.id.asString(), initPlan.lock.asString())
        val client = myRestClient()
        val response = client.post("/plan/update") {
            contentType(ContentType.Application.Json)
            setBody(planUpdateReq)
        }

        val responseBody = response.body<PlanUpdateResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)
        planUpdateReq.run {
            assertEquals(plan?.id, responseBody.plan?.id)
            assertEquals(plan?.conditions, responseBody.plan?.conditions)
            assertEquals(plan?.duration, responseBody.plan?.duration)
            assertEquals(plan?.title, responseBody.plan?.title)
            assertEquals(plan?.visibility, responseBody.plan?.visibility)
            assertEquals(UUID_NEW, responseBody.plan?.lock)
        }
    }

    @Test
    fun `read route`() = testApplication {
        initInMemoryApp(initPlans = initPlans)

        val client = myRestClient()
        val planRequest = getPlanReadReq(TEST_DEBUG, initPlan.id.asString())
        val response = client.post("/plan/read") {
            contentType(ContentType.Application.Json)
            setBody(planRequest)
        }

        val responseBody = response.body<PlanReadResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals(planRequest.plan?.id, responseBody.plan?.id)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)
    }

    @Test
    fun `readAll route`() = testApplication {
        initInMemoryApp(initPlans = initPlans)
        val client = myRestClient()
        val response = client.post("/plan/readAll") {
            contentType(ContentType.Application.Json)
            setBody(getPlanReadAllReq(TEST_DEBUG))
        }

        val responseBody = response.body<PlanReadAllResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertTrue(responseBody.plans?.let { it.size > 1 }  ?: false)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)
    }

    @Test
    fun `delete route`() = testApplication {
        initInMemoryApp(initPlans = initPlans)
        val client = myRestClient()
        val planRequest = getPlanDeleteReq(TEST_DEBUG, initPlan.id.asString(), initPlan.lock.asString())
        val response = client.post("/plan/delete") {
            contentType(ContentType.Application.Json)
            setBody(planRequest)
        }

        val responseBody = response.body<PlanDeleteResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals(planRequest.plan?.id, responseBody.plan?.id)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)
    }

    @Test
    fun `buy route`() = testApplication {
        initInMemoryApp(initPlans = initPlans)

        val planReq = getPlanBuyReq(TEST_DEBUG, initPlan.id.asString())
        val client = myRestClient()
        val response = client.post("/plan/buy") {
            contentType(ContentType.Application.Json)
            setBody(planReq)
        }

        val responseBody = response.body<PlanBuyResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)

        assertEquals(planReq.plan?.id, responseBody.subscription?.planId)
        assertNotEquals(SubscriptionId.NONE.asString(), responseBody.subscription?.id)
        assertNotEquals(SubscriptionLock.NONE.asString(), responseBody.subscription?.lock)
    }

}
package ru.otus

import PlanStubs
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.otuskotlin.subscription.api.v1.models.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlanRoutesTest {

    @Test
    fun `create route`() = testApplication {
        val client = myClient()

        val response = client.post("/plan/create") {
            contentType(ContentType.Application.Json)
            setBody(PLAN_CREATE_REQ)
        }
        val responseBody = response.body<PlanCreateResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals(PlanStubs.PLAN1.id.asString(), responseBody.plan?.id)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)
    }

    @Test
    fun `update route`() = testApplication {
        val client = myClient()
        val response = client.post("/plan/update") {
            contentType(ContentType.Application.Json)
            setBody(PLAN_UPDATE_REQ)
        }

        val responseBody = response.body<PlanUpdateResponse>()
        
        assertEquals(200, response.status.value)
        assertEquals(PlanStubs.PLAN1.id.asString(), responseBody.plan?.id)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)
    }

    @Test
    fun `read route`() = testApplication {
        val client = myClient()
        val response = client.post("/plan/read") {
            contentType(ContentType.Application.Json)
            setBody(PLAN_READ_REQ)
        }

        val responseBody = response.body<PlanReadResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals(PLAN_READ_REQ.plan?.id, responseBody.plan?.id)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)
    }

    @Test
    fun `readAll route`() = testApplication {
        val client = myClient()
        val response = client.post("/plan/readAll") {
            contentType(ContentType.Application.Json)
            setBody(PLAN_READ_ALL_REQ)
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
        val client = myClient()
        val response = client.post("/plan/delete") {
            contentType(ContentType.Application.Json)
            setBody(PLAN_DELETE_REQ)
        }

        val responseBody = response.body<PlanDeleteResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals(PLAN_DELETE_REQ.plan?.id, responseBody.plan?.id)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)
    }

    @Test
    fun `buy route`() = testApplication {
        val client = myClient()
        val response = client.post("/plan/buy") {
            contentType(ContentType.Application.Json)
            setBody(PLAN_BUY_REQ)
        }

        val responseBody = response.body<PlanBuyResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals(PLAN_BUY_REQ.plan?.id, responseBody.subscription?.planId)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)
    }

    
    companion object {

        val PLAN_CREATE_REQ = PlanCreateRequest(
            requestType = "create",
            requestId = "req123",
            debug = DEBUG,
            plan = PlanCreateObject(
                title = "1 month plan",
                conditions = setOf("Surprises", "Good mood"),
                duration = 1,
                price = "500",
                visibility = PlanVisibility.PUBLIC
            )
        )

        val PLAN_UPDATE_REQ = PlanUpdateRequest(
            requestType = "update",
            requestId = "req123",
            debug = DEBUG,
            plan = PlanUpdateObject(
                title = "1 month plan",
                conditions =  setOf("Surprises", "Good mood"),
                duration = 1,
                price = "500",
                visibility = PlanVisibility.PUBLIC,
                id = "plan1"
            )
        )

        val PLAN_READ_REQ = PlanReadRequest(
            requestType = "read",
            requestId = "req123",
            debug = DEBUG,
            plan = PlanReadObject("plan1")
        )

        val PLAN_READ_ALL_REQ = PlanReadAllRequest(
            requestType = "readAll",
            requestId = "req123",
            debug = DEBUG
        )

        val PLAN_DELETE_REQ = PlanDeleteRequest(
            requestType = "delete",
            requestId = "req123",
            debug = DEBUG,
            plan = PlanDeleteObject("plan1")
        )

        val PLAN_BUY_REQ = PlanBuyRequest(
            requestType = "buy",
            requestId = "req123",
            debug = DEBUG,
            plan = PlanBuyObject("plan1")
        )
    }
}

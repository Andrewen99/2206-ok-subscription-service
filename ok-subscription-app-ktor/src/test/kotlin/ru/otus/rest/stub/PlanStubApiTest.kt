package ru.otus.rest.stub

import PlanStubs
import ru.otus.constants.STUB_DEBUG
import ru.otus.constants.getPlanBuyReq
import ru.otus.constants.getPlanCreateReq
import ru.otus.constants.getPlanDeleteReq
import ru.otus.constants.getPlanReadAllReq
import ru.otus.constants.getPlanReadReq
import ru.otus.constants.getPlanUpdateReq
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.otus.initStubApp
import ru.otus.myRestClient
import ru.otuskotlin.subscription.api.v1.models.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlanStubApiTest {

    @Test
    fun `create route`() = testApplication {
        initStubApp()
        val client = myRestClient()

        val response = client.post("/plan/create") {
            contentType(ContentType.Application.Json)
            setBody(getPlanCreateReq(STUB_DEBUG))
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
        initStubApp()
        val client = myRestClient()
        val response = client.post("/plan/update") {
            contentType(ContentType.Application.Json)
            setBody(getPlanUpdateReq(debug = STUB_DEBUG))
        }

        val responseBody = response.body<PlanUpdateResponse>()
        
        assertEquals(200, response.status.value)
        assertEquals(PlanStubs.PLAN1.id.asString(), responseBody.plan?.id)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)
    }

    @Test
    fun `read route`() = testApplication {
        initStubApp()
        val client = myRestClient()
        val response = client.post("/plan/read") {
            contentType(ContentType.Application.Json)
            setBody(getPlanReadReq(STUB_DEBUG))
        }

        val responseBody = response.body<PlanReadResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals(getPlanReadReq(STUB_DEBUG).plan?.id, responseBody.plan?.id)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)
    }

    @Test
    fun `readAll route`() = testApplication {
        initStubApp()
        val client = myRestClient()
        val response = client.post("/plan/readAll") {
            contentType(ContentType.Application.Json)
            setBody(getPlanReadAllReq(STUB_DEBUG))
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
        initStubApp()
        val client = myRestClient()
        val response = client.post("/plan/delete") {
            contentType(ContentType.Application.Json)
            setBody(getPlanDeleteReq(STUB_DEBUG))
        }

        val responseBody = response.body<PlanDeleteResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals(getPlanDeleteReq(STUB_DEBUG).plan?.id, responseBody.plan?.id)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)
    }

    @Test
    fun `buy route`() = testApplication {
        initStubApp()
        val client = myRestClient()
        val response = client.post("/plan/buy") {
            contentType(ContentType.Application.Json)
            setBody(getPlanBuyReq(STUB_DEBUG))
        }

        val responseBody = response.body<PlanBuyResponse>()
        println("\n\n$responseBody\n\n")
        assertEquals(200, response.status.value)
        assertEquals(getPlanBuyReq(STUB_DEBUG).plan?.id, responseBody.subscription?.planId)
        assertEquals("req123", responseBody.requestId)
        assertEquals(ResponseResult.SUCCESS, responseBody.result)
    }

    
}

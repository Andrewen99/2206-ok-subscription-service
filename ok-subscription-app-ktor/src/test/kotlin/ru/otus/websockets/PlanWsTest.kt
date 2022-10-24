package ru.otus.websockets

import org.junit.Test
import ru.otus.ReqConstants
import ru.otuskotlin.subscription.api.v1.models.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlanWsTest {

    @Test
    fun create() = wsTest<PlanCreateResponse>("/plan", ReqConstants.PLAN_CREATE_REQ) { response ->
        println("\n\n$response\n\n")
        assertEquals(PlanStubs.PLAN1.id.asString(), response.plan?.id)
        assertEquals("req123", response.requestId)
        assertEquals(ResponseResult.SUCCESS, response.result)
    }


    @Test
    fun update() = wsTest<PlanUpdateResponse>("/plan", ReqConstants.PLAN_UPDATE_REQ) { response ->
        println("\n\n$response\n\n")
        assertEquals(PlanStubs.PLAN1.id.asString(), response.plan?.id)
        assertEquals("req123", response.requestId)
        assertEquals(ResponseResult.SUCCESS, response.result)
    }

    @Test
    fun read() = wsTest<PlanReadResponse>("/plan", ReqConstants.PLAN_READ_REQ) { response ->
        println("\n\n$response\n\n")
        assertEquals(ReqConstants.PLAN_READ_REQ.plan?.id, response.plan?.id)
        assertEquals("req123", response.requestId)
        assertEquals(ResponseResult.SUCCESS, response.result)
    }

    @Test
    fun readAll1() = wsTest<PlanReadAllResponse>("/plan", ReqConstants.PLAN_READ_ALL_REQ) { response ->
        println("\n\n$response\n\n")
        assertTrue(response.plans?.let { it.size > 1 }  ?: false)
        assertEquals("req123", response.requestId)
        assertEquals(ResponseResult.SUCCESS, response.result)
    }

    @Test
    fun delete() = wsTest<PlanDeleteResponse>("/plan", ReqConstants.PLAN_DELETE_REQ) { response ->
        println("\n\n$response\n\n")
        assertEquals(ReqConstants.PLAN_DELETE_REQ.plan?.id, response.plan?.id)
        assertEquals("req123", response.requestId)
        assertEquals(ResponseResult.SUCCESS, response.result)
    }

}
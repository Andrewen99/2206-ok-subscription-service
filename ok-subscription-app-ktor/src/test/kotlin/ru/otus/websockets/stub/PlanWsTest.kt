package ru.otus.websockets.stub

import ru.otus.constants.STUB_DEBUG
import ru.otus.constants.getPlanCreateReq
import ru.otus.constants.getPlanDeleteReq
import ru.otus.constants.getPlanReadAllReq
import ru.otus.constants.getPlanReadReq
import ru.otus.constants.getPlanUpdateReq
import org.junit.Test
import ru.otuskotlin.subscription.api.v1.models.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlanWsTest {

    @Test
    fun create() = wsTest<PlanCreateResponse>("/plan", getPlanCreateReq(STUB_DEBUG)) { response ->
        println("\n\n$response\n\n")
        assertEquals(PlanStubs.PLAN1.id.asString(), response.plan?.id)
        assertEquals("req123", response.requestId)
        assertEquals(ResponseResult.SUCCESS, response.result)
    }


    @Test
    fun update() = wsTest<PlanUpdateResponse>("/plan", getPlanUpdateReq(debug = STUB_DEBUG)) { response ->
        println("\n\n$response\n\n")
        assertEquals(PlanStubs.PLAN1.id.asString(), response.plan?.id)
        assertEquals("req123", response.requestId)
        assertEquals(ResponseResult.SUCCESS, response.result)
    }

    @Test
    fun read() = wsTest<PlanReadResponse>("/plan", getPlanReadReq(STUB_DEBUG)) { response ->
        println("\n\n$response\n\n")
        assertEquals(getPlanReadReq(STUB_DEBUG).plan?.id, response.plan?.id)
        assertEquals("req123", response.requestId)
        assertEquals(ResponseResult.SUCCESS, response.result)
    }

    @Test
    fun readAll1() = wsTest<PlanReadAllResponse>("/plan", getPlanReadAllReq(STUB_DEBUG)) { response ->
        println("\n\n$response\n\n")
        assertTrue(response.plans?.let { it.size > 1 }  ?: false)
        assertEquals("req123", response.requestId)
        assertEquals(ResponseResult.SUCCESS, response.result)
    }

    @Test
    fun delete() = wsTest<PlanDeleteResponse>("/plan", getPlanDeleteReq(STUB_DEBUG)) { response ->
        println("\n\n$response\n\n")
        assertEquals(getPlanDeleteReq(STUB_DEBUG).plan?.id, response.plan?.id)
        assertEquals("req123", response.requestId)
        assertEquals(ResponseResult.SUCCESS, response.result)
    }

}
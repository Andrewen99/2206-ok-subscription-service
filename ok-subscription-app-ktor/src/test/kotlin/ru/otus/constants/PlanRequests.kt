package ru.otus.constants

import ru.otuskotlin.subscription.api.v1.models.*

fun getPlanCreateReq(debug: Debug) = PlanCreateRequest(
    requestType = "create",
    requestId = "req123",
    debug = debug,
    plan = PlanCreateObject(
        title = "1 month plan",
        conditions = setOf("Surprises", "Good mood"),
        duration = 1,
        price = "500",
        visibility = PlanVisibility.PUBLIC
    )
)

fun getPlanUpdateReq(debug: Debug, planId: String = "plan1") = PlanUpdateRequest(
    requestType = "update",
    requestId = "req123",
    debug = debug,
    plan = PlanUpdateObject(
        title = "1 month plan",
        conditions = setOf("Surprises", "Good mood"),
        duration = 1,
        price = "500",
        visibility = PlanVisibility.PUBLIC,
        id = planId
    )
)

fun getPlanReadReq(debug: Debug = STUB_DEBUG, planId: String = "plan1") = PlanReadRequest(
    requestType = "read",
    requestId = "req123",
    debug = debug,
    plan = PlanReadObject(planId)
)

fun getPlanReadAllReq(debug: Debug) = PlanReadAllRequest(
    requestType = "readAll",
    requestId = "req123",
    debug = debug
)

fun getPlanDeleteReq(debug: Debug = STUB_DEBUG, planId: String = "plan1") = PlanDeleteRequest(
    requestType = "delete",
    requestId = "req123",
    debug = debug,
    plan = PlanDeleteObject(planId)
)

fun getPlanBuyReq(debug: Debug = STUB_DEBUG, planId: String = "plan1") = PlanBuyRequest(
    requestType = "buy",
    requestId = "req123",
    debug = debug,
    plan = PlanBuyObject(planId)
)
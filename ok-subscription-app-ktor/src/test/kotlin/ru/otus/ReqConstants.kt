package ru.otus

import ru.otuskotlin.subscription.api.v1.models.*

object ReqConstants {

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

    val SUBSCRIPTION_READ_REQ = SubscriptionReadRequest(
        requestType = "read",
        requestId = "req123",
        debug = DEBUG,
        subscription = SubscriptionReadObject("sub1")
    )

    val SUBSCRIPTION_SEARCH_REQ = SubscriptionSearchRequest(
        requestType = "search",
        requestId = "req123",
        debug = DEBUG,
        subscriptionFilter = SubscriptionSearchFilter(
            ownerId = "owner1",
            boughtPeriod = FromToDateObject("01.01.2022", "01.01.2023"),
            isActive = true
        )
    )

    val SUBSCRIPTION_PAY_REQ = SubscriptionPayRequest(
        requestType = "pay",
        requestId = "req123",
        debug = DEBUG,
        subscription = SubscriptionPayObject("sub1")
    )
}

package ru.otus.constants

import ru.otuskotlin.subscription.api.v1.models.*

fun getSubscriptionReadReq(id: String = "sub1", debug: Debug = STUB_DEBUG) = SubscriptionReadRequest(
    requestType = "read",
    requestId = "req123",
    debug = debug,
    subscription = SubscriptionReadObject(id)
)

fun getSubscriptionSearchReq(debug: Debug) = SubscriptionSearchRequest(
    requestType = "search",
    requestId = "req123",
    debug = debug,
    subscriptionFilter = SubscriptionSearchFilter(
        isActive = false
    )
)


fun getSubscriptionPayReq(id: String = "sub1", debug: Debug = STUB_DEBUG) = SubscriptionPayRequest(
    requestType = "pay",
    requestId = "req123",
    debug = debug,
    subscription = SubscriptionPayObject(id=id)
)



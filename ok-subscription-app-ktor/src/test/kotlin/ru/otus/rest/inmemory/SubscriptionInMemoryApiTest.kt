package ru.otus.rest.inmemory

import io.ktor.server.testing.*
import models.plan.Plan
import models.subscription.Subscription
import ru.otus.initInMemoryApp
import ru.otus.rest.SubscriptionApiTest


class SubscriptionInMemoryApiTest : SubscriptionApiTest() {
    override fun TestApplicationBuilder.initApp(initPlans: List<Plan>, initSubscriptions: List<Subscription>) {
        initInMemoryApp(initPlans, initSubscriptions)
    }
}
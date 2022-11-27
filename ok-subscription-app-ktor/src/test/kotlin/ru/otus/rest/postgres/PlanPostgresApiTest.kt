package ru.otus.rest.postgres

import io.ktor.server.testing.*
import models.plan.Plan
import models.subscription.Subscription
import ru.otus.initPostgresApp
import ru.otus.rest.PlanApiTest

class PlanPostgresApiTest: PlanApiTest() {
    override fun TestApplicationBuilder.initApp(initPlans: List<Plan>, initSubscriptions: List<Subscription>) {
        initPostgresApp(initPlans, initSubscriptions)
    }
}
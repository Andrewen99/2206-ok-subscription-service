package ru.otus

import io.ktor.server.application.*
import models.plan.Plan
import models.plan.PlanId
import ru.otus.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureSerialization()
    configureRouting()

}

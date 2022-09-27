package ru.otus

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import ru.otuskotlin.subscription.api.v1.models.Debug
import ru.otuskotlin.subscription.api.v1.models.RequestDebugMode
import ru.otuskotlin.subscription.api.v1.models.RequestDebugStubs

fun ApplicationTestBuilder.myClient() = createClient {
    install(ContentNegotiation) {
        jackson {
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            enable(SerializationFeature.INDENT_OUTPUT)
            writerWithDefaultPrettyPrinter()
        }
    }
}


internal val DEBUG = Debug(
    mode = RequestDebugMode.STUB,
    stub = RequestDebugStubs.SUCCESS
)
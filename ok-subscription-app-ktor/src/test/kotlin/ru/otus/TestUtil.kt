package ru.otus

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import io.ktor.client.plugins.websocket.*
import ru.otuskotlin.subscription.api.v1.models.Debug
import ru.otuskotlin.subscription.api.v1.models.RequestDebugMode
import ru.otuskotlin.subscription.api.v1.models.RequestDebugStubs

fun ApplicationTestBuilder.myRestClient() = createClient {
    install(ContentNegotiation) {
        jackson {
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            enable(SerializationFeature.INDENT_OUTPUT)
            writerWithDefaultPrettyPrinter()
        }
    }
}

fun ApplicationTestBuilder.myWsClient() = createClient {
    install(WebSockets)
}


internal val DEBUG = Debug(
    mode = RequestDebugMode.STUB,
    stub = RequestDebugStubs.SUCCESS
)
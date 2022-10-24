package ru.otus.websockets

import apiV1Mapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.client.plugins.websocket.*
import io.ktor.server.testing.*
import io.ktor.websocket.*
import kotlinx.coroutines.time.withTimeout
import ru.otus.myWsClient
import ru.otuskotlin.subscription.api.v1.models.*
import java.time.Duration
import kotlin.test.assertIs

inline fun <reified R: IResponse> wsTest(endpoint: String, request: IRequest, crossinline assertions: (R) -> Unit) = testApplication {
    val client = myWsClient()

    client.webSocket(endpoint) {
        withTimeout(Duration.ofSeconds(1)) {
            val initReceived = incoming.receive()
            val initResponse = apiV1Mapper.readValue((initReceived as Frame.Text).readText()) as IResponse
            assertIs<WsInitResponse>(initResponse)
        }

        send(Frame.Text(apiV1Mapper.writeValueAsString(request)))

        withTimeout(Duration.ofSeconds(1)) {
            val received = incoming.receive()
            val response = apiV1Mapper.readValue((received as Frame.Text).readText(), R::class.java)
            assertions(response)
        }
    }
}
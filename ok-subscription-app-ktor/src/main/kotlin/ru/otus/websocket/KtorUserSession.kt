package ru.otus.websocket

import io.ktor.websocket.*
import models.IClientSession

data class KtorUserSession(
    override val fwSession: WebSocketSession,
    override val apiVersion: String = "v1",
): IClientSession<WebSocketSession>

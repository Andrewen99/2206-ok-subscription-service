package ru.otus.settings

import io.ktor.server.application.*

data class KtorPostgresConfig(
    val url: String,
    val user: String,
    val password: String,
    val schema: String,
)
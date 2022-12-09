package ru.otus.settings

import io.ktor.server.application.*

data class KtorPostgresConfig(
    val url: String,
    val user: String,
    val password: String,
    val schema: String,
) {
    constructor(environment: ApplicationEnvironment) : this(
        url = environment.config.property("postgres.url").getString(),
        user = environment.config.property("postgres.user").getString(),
        password = environment.config.property("postgres.password").getString(),
        schema = environment.config.property("postgres.schema").getString()
    )
}
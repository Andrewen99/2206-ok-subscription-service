package ru.otus.settings

import io.ktor.server.application.*


fun createAuthConfigFromEnvironment(environment: ApplicationEnvironment) = KtorAuthConfig(
    secret = environment.config.propertyOrNull("jwt.secret")?.getString() ?: "",
    issuer = environment.config.property("jwt.issuer").getString(),
    audience = environment.config.property("jwt.audience").getString(),
    realm = environment.config.property("jwt.realm").getString(),
    clientId = environment.config.property("jwt.clientId").getString(),
    certUrl = environment.config.propertyOrNull("jwt.certUrl")?.getString()
)

fun createPostgresConfigFromEnvironment(environment: ApplicationEnvironment) = KtorPostgresConfig(
    url = environment.config.property("postgres.url").getString(),
    user = environment.config.property("postgres.user").getString(),
    password = environment.config.property("postgres.password").getString(),
    schema = environment.config.property("postgres.schema").getString()
)

package ru.otus.utils

import io.ktor.server.auth.jwt.*
import models.SbscrPrincipalModel
import models.SbscrUserId
import permissions.SbscrUserGroups
import ru.otus.settings.KtorAuthConfig.Companion.F_NAME_CLAIM
import ru.otus.settings.KtorAuthConfig.Companion.GROUPS_CLAIM
import ru.otus.settings.KtorAuthConfig.Companion.ID_CLAIM
import ru.otus.settings.KtorAuthConfig.Companion.L_NAME_CLAIM
import ru.otus.settings.KtorAuthConfig.Companion.M_NAME_CLAIM

fun JWTPrincipal?.toModel() = this?.run {
    SbscrPrincipalModel(
        id = payload.getClaim(ID_CLAIM).asString()?.let { SbscrUserId(it) } ?: SbscrUserId.NONE,
        fname = payload.getClaim(F_NAME_CLAIM).asString() ?: "",
        mname = payload.getClaim(M_NAME_CLAIM).asString() ?: "",
        lname = payload.getClaim(L_NAME_CLAIM).asString() ?: "",
        groups = payload
            .getClaim(GROUPS_CLAIM)
            ?.asList(String::class.java)
            ?.mapNotNull {
                when(it) {
                    "USER" -> SbscrUserGroups.USER
                    "ADMIN" -> SbscrUserGroups.ADMIN
                    "MODERATOR" -> SbscrUserGroups.MODERATOR
                    else -> null
                }
            }?.toSet() ?: emptySet()
    )
} ?: SbscrPrincipalModel.NONE
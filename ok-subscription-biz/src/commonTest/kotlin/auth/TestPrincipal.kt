package auth

import models.SbscrPrincipalModel
import models.SbscrUserId
import permissions.SbscrUserGroups

fun createPrincipal(
    id: SbscrUserId = SbscrUserId("123"),
    groups: Set<SbscrUserGroups> = setOf(SbscrUserGroups.USER)
) = SbscrPrincipalModel(id = id, groups = groups)

fun createPrincipal(
    id: SbscrUserId = SbscrUserId("123"),
    group: SbscrUserGroups = SbscrUserGroups.USER
) = SbscrPrincipalModel(id = id, groups = setOf(group))
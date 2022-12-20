package subscription

import permissions.SbscrUserGroups
import permissions.UserSubscriptionPermissions

fun resolveSubscriptionChainPermissions(
    groups: Iterable<SbscrUserGroups>
) = mutableSetOf<UserSubscriptionPermissions>()
    .apply {
        addAll(groups.flatMap {  groupPermissionAdmits[it] ?: emptySet() })
        removeAll(groups.flatMap { groupPermissionsDenies[it] ?: emptySet() }.toSet())
    }.toSet()

private val groupPermissionAdmits = mapOf(
    SbscrUserGroups.USER to setOf(
        UserSubscriptionPermissions.READ_OWN,
        UserSubscriptionPermissions.CREATE_OWN,
        UserSubscriptionPermissions.UPDATE_OWN,
        UserSubscriptionPermissions.SEARCH_OWN
    ),
    SbscrUserGroups.ADMIN to setOf(
        UserSubscriptionPermissions.CREATE_OWN,
        UserSubscriptionPermissions.READ_OWN,
        UserSubscriptionPermissions.READ_CANDIDATE,
        UserSubscriptionPermissions.UPDATE_OWN,
        UserSubscriptionPermissions.UPDATE_CANDIDATE,
        UserSubscriptionPermissions.SEARCH,
        UserSubscriptionPermissions.SEARCH_OWN,
        UserSubscriptionPermissions.DELETE_CANDIDATE,
    ),

    SbscrUserGroups.MODERATOR to setOf(
        UserSubscriptionPermissions.CREATE_OWN,
        UserSubscriptionPermissions.READ_OWN,
        UserSubscriptionPermissions.UPDATE_OWN,
        UserSubscriptionPermissions.READ_CANDIDATE,
        UserSubscriptionPermissions.UPDATE_CANDIDATE,
        UserSubscriptionPermissions.SEARCH,
        UserSubscriptionPermissions.SEARCH_OWN
    ),
    SbscrUserGroups.TEST to setOf(),
    SbscrUserGroups.BAN to setOf()
)

private val groupPermissionsDenies = mapOf<SbscrUserGroups, Set<UserSubscriptionPermissions>>(
    SbscrUserGroups.USER to setOf(),
    SbscrUserGroups.ADMIN to setOf(),
    SbscrUserGroups.MODERATOR to setOf(),
    SbscrUserGroups.TEST to setOf(),
    SbscrUserGroups.BAN to setOf(
        UserSubscriptionPermissions.CREATE_OWN,
        UserSubscriptionPermissions.READ_OWN,
        UserSubscriptionPermissions.UPDATE_OWN
    )
)
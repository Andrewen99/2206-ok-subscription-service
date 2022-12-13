import permissions.SbscrUserGroups
import permissions.UserPlanPermissions

fun resolvePlanChainPermissions(
    groups: Iterable<SbscrUserGroups>
) = mutableSetOf<UserPlanPermissions>()
    .apply {
        addAll(groups.flatMap {  groupPermissionAdmits[it] ?: emptySet() })
        removeAll(groups.flatMap { groupPermissionsDenies[it] ?: emptySet() }.toSet())
    }.toSet()

private val groupPermissionAdmits = mapOf(
    SbscrUserGroups.USER to setOf(
        UserPlanPermissions.READ_PUBLIC
    ),
    SbscrUserGroups.ADMIN to setOf(
        UserPlanPermissions.READ_PUBLIC,
        UserPlanPermissions.READ_ADMIN_ONLY,
        UserPlanPermissions.READ_CANDIDATE,
        UserPlanPermissions.CREATE,
        UserPlanPermissions.UPDATE_CANDIDATE,
        UserPlanPermissions.DELETE_CANDIDATE,
    ),

    SbscrUserGroups.MODERATOR to setOf(
        UserPlanPermissions.READ_PUBLIC,
        UserPlanPermissions.READ_CANDIDATE,
        UserPlanPermissions.UPDATE_CANDIDATE,
        UserPlanPermissions.DELETE_CANDIDATE,
    ),
    SbscrUserGroups.TEST to setOf(),
    SbscrUserGroups.BAN to setOf()
)

private val groupPermissionsDenies = mapOf<SbscrUserGroups, Set<UserPlanPermissions>>(
    SbscrUserGroups.USER to setOf(),
    SbscrUserGroups.ADMIN to setOf(),
    SbscrUserGroups.MODERATOR to setOf(),
    SbscrUserGroups.TEST to setOf(),
    SbscrUserGroups.BAN to setOf(
        UserPlanPermissions.CREATE,
        UserPlanPermissions.UPDATE_CANDIDATE,
        UserPlanPermissions.DELETE_CANDIDATE,
    )
)
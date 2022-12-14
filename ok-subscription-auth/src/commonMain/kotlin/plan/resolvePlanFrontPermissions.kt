package plan

import models.plan.Plan
import permissions.PlanPermissionsClient
import permissions.SbscrPrincipalRelations
import permissions.UserPlanPermissions

fun resolvePlanFrontPermissions(
    permissions: Iterable<UserPlanPermissions>,
    relations: Iterable<SbscrPrincipalRelations>
) = mutableSetOf<PlanPermissionsClient>()
    .apply {
        for (permission in permissions) {
            for (relation in relations) {
                accessTable[permission]?.get(relation)?.let { this@apply.add(it) }
            }
        }
    }
    .toSet()


private val accessTable = mapOf(
    //read
    UserPlanPermissions.READ_PUBLIC to mapOf(
        SbscrPrincipalRelations.PUBLIC to PlanPermissionsClient.READ
    ),

    UserPlanPermissions.READ_ADMIN_ONLY to mapOf(
        SbscrPrincipalRelations.FOR_ADMINS to PlanPermissionsClient.READ
    ),

    //update
    UserPlanPermissions.UPDATE_CANDIDATE to mapOf(
        SbscrPrincipalRelations.MODERATABLE to PlanPermissionsClient.UPDATE,
        SbscrPrincipalRelations.FOR_ADMINS to PlanPermissionsClient.UPDATE
    ),

    //delete
    UserPlanPermissions.DELETE_CANDIDATE to mapOf(
        SbscrPrincipalRelations.MODERATABLE to PlanPermissionsClient.DELETE
    )
)
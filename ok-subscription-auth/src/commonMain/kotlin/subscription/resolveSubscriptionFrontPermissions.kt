package subscription

import models.plan.Plan
import permissions.*

fun resolveSubscriptionFrontPermissions(
    permissions: Iterable<UserSubscriptionPermissions>,
    relations: Iterable<SbscrPrincipalRelations>
) = mutableSetOf<SubscriptionPermissionsClient>()
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
    UserSubscriptionPermissions.READ_OWN to mapOf(
        SbscrPrincipalRelations.OWN to SubscriptionPermissionsClient.READ
    ),

    UserSubscriptionPermissions.READ_CANDIDATE to mapOf(
        SbscrPrincipalRelations.MODERATABLE to SubscriptionPermissionsClient.READ
    ),

    //update
    UserSubscriptionPermissions.UPDATE_CANDIDATE to mapOf(
        SbscrPrincipalRelations.MODERATABLE to SubscriptionPermissionsClient.UPDATE
    ),

    //delete
    UserSubscriptionPermissions.DELETE_CANDIDATE to mapOf(
        SbscrPrincipalRelations.MODERATABLE to SubscriptionPermissionsClient.DELETE
    ),

    //search
    UserSubscriptionPermissions.SEARCH_OWN to mapOf(
        SbscrPrincipalRelations.PUBLIC to SubscriptionPermissionsClient.SEARCH
    ),
    UserSubscriptionPermissions.SEARCH to mapOf(
        SbscrPrincipalRelations.MODERATABLE to SubscriptionPermissionsClient.SEARCH
    )
)
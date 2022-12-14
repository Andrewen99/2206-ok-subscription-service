package subscription

import models.subscription.SubscriptionCommand
import permissions.SbscrPrincipalRelations
import permissions.UserSubscriptionPermissions

fun checkSubscriptionPermitted(
    command: SubscriptionCommand,
    relations: Iterable<SbscrPrincipalRelations>,
    permissions: Iterable<UserSubscriptionPermissions>
) =
    relations.asSequence().flatMap { relation ->
        permissions.map { permission ->
            SubscriptionTableConditions(
                command = command,
                permission = permission,
                relation = relation
            )
        }
    }.any {
        accessTable[it] != null
    }

private data class SubscriptionTableConditions(
    val command: SubscriptionCommand,
    val permission: UserSubscriptionPermissions,
    val relation: SbscrPrincipalRelations
)

private val accessTable = mapOf(
    //create/buy
    SubscriptionTableConditions(
        command = SubscriptionCommand.BUY,
        permission = UserSubscriptionPermissions.CREATE_OWN,
        relation = SbscrPrincipalRelations.NEW
    ) to true,

    SubscriptionTableConditions(
        command = SubscriptionCommand.PAY,
        permission = UserSubscriptionPermissions.UPDATE_OWN,
        relation = SbscrPrincipalRelations.OWN
    ) to true,

    //read
    SubscriptionTableConditions(
        command = SubscriptionCommand.READ,
        permission = UserSubscriptionPermissions.READ_OWN,
        relation = SbscrPrincipalRelations.OWN
    ) to true,

    SubscriptionTableConditions(
        command = SubscriptionCommand.READ,
        permission = UserSubscriptionPermissions.READ_CANDIDATE,
        relation = SbscrPrincipalRelations.MODERATABLE
    ) to true,


    //search
    SubscriptionTableConditions(
        command = SubscriptionCommand.SEARCH,
        permission = UserSubscriptionPermissions.SEARCH,
        relation = SbscrPrincipalRelations.MODERATABLE
    ) to true,
)
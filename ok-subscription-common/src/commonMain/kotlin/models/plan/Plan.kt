package models.plan

import permissions.PlanPermissionsClient
import permissions.SbscrPrincipalRelations

/**
 * План подписки
 */
data class Plan(
    var id: PlanId = PlanId.NONE,
    var title: String = "",
    var conditions: MutableSet<String> = mutableSetOf(),
    var duration: Int = 0,
    var price: String = "0",
    var lock: PlanLock = PlanLock.NONE,
    var visibility: SbscrPlanVisibility = SbscrPlanVisibility.NONE,
    var principalRelations: Set<SbscrPrincipalRelations> = emptySet(),
    val permissionsClient: MutableSet<PlanPermissionsClient> = mutableSetOf(),
) {
    fun deepCopy() = copy(
        conditions = conditions.toMutableSet(),
        principalRelations = principalRelations.toSet(),
        permissionsClient = permissionsClient.toMutableSet()
    )
}


package models.subscription

import util.MIN_LOCAL_DATE
import kotlinx.datetime.LocalDate
import models.SbscrUserId
import models.plan.PlanId
import permissions.SbscrPrincipalRelations
import permissions.SubscriptionPermissionsClient

/**
 * Приобретенная подписка
 */
data class Subscription(
    var id: SubscriptionId = SubscriptionId.NONE,
    var ownerId: SbscrUserId = SbscrUserId.NONE,
    var planId: PlanId = PlanId.NONE,
    var startDate: LocalDate = MIN_LOCAL_DATE,
    var endDate: LocalDate = MIN_LOCAL_DATE,
    var isActive: Boolean = false,
    var lock: SubscriptionLock = SubscriptionLock.NONE,
    var paymentStatus: SbscrPaymentStatus = SbscrPaymentStatus.NOT_PAYED,
    var principalRelations: Set<SbscrPrincipalRelations> = emptySet(),
    var permissionsClient: MutableSet<SubscriptionPermissionsClient> = mutableSetOf()
) {
    fun deepCopy() = copy(
        principalRelations = principalRelations.toSet(),
        permissionsClient = permissionsClient.toMutableSet()
    )
}
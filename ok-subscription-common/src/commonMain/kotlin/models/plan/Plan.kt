package models.plan

/**
 * План подписки
 */
data class Plan(
    var id: PlanId = PlanId.NONE,
    var title: String = "",
    val conditions: MutableSet<String> = mutableSetOf(),
    var duration: Int = 0,
    var price: String = "0",
    var lock: PlanLock = PlanLock.NONE,
    var visibility: SbscrPlanVisibility = SbscrPlanVisibility.NONE
) {
    fun deepCopy() = copy(
        conditions = conditions.toMutableSet()
    )
}


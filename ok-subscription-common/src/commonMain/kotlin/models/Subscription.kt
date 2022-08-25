package models

/**
 * Подписка
 */
data class Subscription(
    var id: SbscrId = SbscrId.NONE,
    var title: String = "",
    val conditions: MutableSet<String> = mutableSetOf(),
    var duration: Int = 0,
    var price: String = "0",
    var visibility: SbscrVisibility = SbscrVisibility.NONE
)


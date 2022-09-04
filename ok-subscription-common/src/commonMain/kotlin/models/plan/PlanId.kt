package models.plan

/**
 * Id подписки
 */
@JvmInline
value class PlanId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = PlanId("")
    }
}
package models.subscription

/**
 * Id приобретенной подписки
 */
@JvmInline
value class SubscriptionId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = SubscriptionId("")
    }
}
package models.subscription

import kotlin.jvm.JvmInline

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
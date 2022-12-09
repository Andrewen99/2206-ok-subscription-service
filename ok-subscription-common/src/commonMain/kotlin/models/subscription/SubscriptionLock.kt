package models.subscription

import kotlin.jvm.JvmInline

@JvmInline
value class SubscriptionLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = SubscriptionLock("")
    }
}
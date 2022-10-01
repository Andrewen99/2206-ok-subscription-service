package models.plan

import kotlin.jvm.JvmInline

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
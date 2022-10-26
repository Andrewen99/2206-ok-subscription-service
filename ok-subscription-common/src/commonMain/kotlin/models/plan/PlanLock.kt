package models.plan

import kotlin.jvm.JvmInline

@JvmInline
value class PlanLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = PlanLock("")
    }
}
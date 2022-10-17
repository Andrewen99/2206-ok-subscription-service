package models

import kotlin.jvm.JvmInline

/**
 * Id пользователя
 */
@JvmInline
value class SbscrUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = SbscrUserId("")
    }
}

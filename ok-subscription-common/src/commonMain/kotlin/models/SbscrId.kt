package models

/**
 * Id подписки
 */
@JvmInline
value class SbscrId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = SbscrId("")
    }
}
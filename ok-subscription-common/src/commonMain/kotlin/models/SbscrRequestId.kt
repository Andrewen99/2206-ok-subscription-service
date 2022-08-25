package models

/**
 * Id запроса
 */
@JvmInline
value class SbscrRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = SbscrRequestId("")
    }
}
package models

/**
 * Id приобретенной подписки
 */
@JvmInline
value class AcqSbscrId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = AcqSbscrId("")
    }
}
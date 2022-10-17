package models

data class SbscrError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
    val level: Levels = Levels.ERROR
) {
    @Suppress("unused")
    enum class Levels {
        ERROR,
        WARNING,
        INFO,
    }
}

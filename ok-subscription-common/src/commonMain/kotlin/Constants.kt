import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

private val INSTANT_NONE = Instant.fromEpochMilliseconds(Long.MIN_VALUE)
val Instant.Companion.NONE
    get() = INSTANT_NONE

val MIN_LOCAL_DATE = LocalDate(-999_999, 1, 1)
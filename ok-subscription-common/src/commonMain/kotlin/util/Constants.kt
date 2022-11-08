package util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

private val INSTANT_NONE = Instant.fromEpochMilliseconds(Long.MIN_VALUE)
val Instant.Companion.NONE
    get() = INSTANT_NONE

val MIN_LOCAL_DATE = LocalDate(-999_999, 1, 1)

val ID_FORMAT_REGEX = Regex("^[0-9a-zA-Z-]+$")

//\p{L} matches a single code point in the category "letter".
val HAS_CONTENT_REGEX = Regex("\\p{L}")
val PRICE_FORMAT_REGEX = Regex("^\\d+(,\\d{1,2})?$")
package models

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Класс хранящий период
 */
data class SbscrDatePeriod(
    var startDate: LocalDate = LocalDate.MIN,
    var endDate: LocalDate = LocalDate.MIN
)

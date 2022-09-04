package models

import java.time.LocalDate

/**
 * Класс хранящий период
 */
data class SbscrDatePeriod(
    var startDate: LocalDate = LocalDate.MIN,
    var endDate: LocalDate = LocalDate.MIN
)

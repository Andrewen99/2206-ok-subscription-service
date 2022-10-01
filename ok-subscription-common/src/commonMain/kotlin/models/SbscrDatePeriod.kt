package models

import MIN_LOCAL_DATE
import kotlinx.datetime.LocalDate


/**
 * Класс хранящий период
 */
data class SbscrDatePeriod(
    var startDate: LocalDate = MIN_LOCAL_DATE,
    var endDate: LocalDate = MIN_LOCAL_DATE
)



package util

import kotlinx.datetime.*
import models.SbscrDatePeriod

fun getYesterdayAsLocalDate(): LocalDate {
    return Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
        .minus(1, DateTimeUnit.DAY)
}

fun getLastMonthAsLocalDate(): LocalDate {
    return Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
        .minus(1, DateTimeUnit.MONTH)
}

fun getNextMonthAsLocalDate(): LocalDate {
    return Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
        .plus(1, DateTimeUnit.MONTH)
}

infix fun LocalDate.inPeriod(period: SbscrDatePeriod): Boolean {
    return this >= period.startDate && this <= period.endDate
}
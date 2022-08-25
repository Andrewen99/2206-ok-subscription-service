package models

/**
 * Фильтр для поиска приобретенных подписок
 */
data class AcqSbscrFilter(
    var ownerId: SbscrUserId = SbscrUserId.NONE,
    var boughtPeriod: SbscrDatePeriod? = null,
    var expirationPeriod: SbscrDatePeriod? = null,
    var isActive: Boolean? = null
)
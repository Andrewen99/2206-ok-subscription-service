package models

/**
 * Стабы ответов
 */
enum class SbscrStubs {
    NONE,
    SUCCESS,
    NOT_FOUND,
    BAD_ID,
    BAD_TITLE,
    BAD_CONDITION,
    BAD_VISIBILITY,
    CANNOT_DELETE,
    BAD_SEARCH_PARAMETERS,
    CANNOT_BUY,
    PAYMENT_ERROR,
    DB_ERROR
}

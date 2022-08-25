package models

/**
 * Типы запросов для приобретенных/приобретаемых подписок
 */
enum class AcqSbscrCommand {
    BUY, //приобретение
    SEARCH_ACQUIRED, //поиск приобретенных подписок
    NONE
}
package models.subscription

/**
 * Типы запросов для приобретенных/приобретаемых подписок
 */
enum class SubscriptionCommand {
    BUY, //приобретение
    SEARCH, //поиск приобретенных подписок
    READ,
    PAY,
    NONE
}
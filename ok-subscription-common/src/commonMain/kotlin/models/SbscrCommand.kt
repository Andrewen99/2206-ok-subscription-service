package models

/**
 * Типы запросов для взаимодействия с подписками
 */
enum class SbscrCommand {
    NONE,
    CREATE,
    UPDATE,
    READ,
    READ_ALL,
    DELETE
}
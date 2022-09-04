package models.plan

/**
 * Типы запросов для взаимодействия с подписками
 */
enum class PlanCommand {
    NONE,
    CREATE,
    UPDATE,
    READ,
    READ_ALL,
    DELETE
}
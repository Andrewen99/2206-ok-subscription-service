package models

/**
 * Состояние обработки запроса
 */
enum class SbscrState {
    NONE,
    RUNNING,
    FAILING,
    FINISHING,
}
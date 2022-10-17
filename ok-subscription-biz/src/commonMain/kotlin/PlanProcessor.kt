import contexts.PlanContext
import dsl.rootChain
import dsl.worker
import general.operation
import models.plan.PlanCommand
import models.plan.PlanId
import stubs.*
import stubs.plan.*
import validation.finishPlanValidation
import validation.plan.*
import validation.validation

class PlanProcessor {
    suspend fun exec(ctx: PlanContext) = PlanChain.exec(ctx)

    companion object {
        private val PlanChain = rootChain<PlanContext> {
            initStatus("Инициализация цепи")

            operation("Создание плана", PlanCommand.CREATE) {
                stubs("Обработка стабов") {
                    stubCreateSuccess("Имитация успешной обработки")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadCondition("Имитация ошибки валидации условия плана")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }

                validation {
                    worker("Копируем поля в PlanValidating") { planValidating = planRequest.deepCopy() }
                    worker("Очистка id") { planValidating.id = PlanId(planValidating.id.asString().trim()) }
                    worker("Очистка названия плана") { planValidating.title = planValidating.title.trim() }
                    worker("Очистка цены") { planValidating.price = planValidating.price.trim() }
                    worker("Очистка условий подписки") {
                        val tempConditions = planValidating.conditions.map { it.trim() }.toMutableSet()
                        planValidating.conditions.clear()
                        planValidating.conditions.addAll(tempConditions)
                    }

                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validatePriceNotEmpty("Проверка, что цена не пуста")
                    validateConditionsNotEmpty("Проверка, что условия не пусты")

                    validateTitleHasContent("Проверка присутствия текста в заголовке")
                    validateDurationIsPositive("Проверка позитивного значения длительности плана")
                    validatePriceProperFormat("Проверка формата цены подписки")
                    validateConditions("Проверка условий подписки")

                    finishPlanValidation("Завершение валидации")
                }
            }

            operation("Обновление плана", PlanCommand.UPDATE) {
                stubs("Обработка стабов") {
                    stubUpdateSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadCondition("Имитация ошибки валидации условия плана")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }

                validation {
                    worker("Копируем поля в PlanValidating") { planValidating = planRequest.deepCopy() }
                    worker("Очистка id") { planValidating.id = PlanId(planValidating.id.asString().trim()) }
                    worker("Очистка названия плана") { planValidating.title = planValidating.title.trim() }
                    worker("Очистка цены") { planValidating.price = planValidating.price.trim() }
                    worker("Очистка условий подписки") {
                        val tempConditions = planValidating.conditions.map { it.trim() }.toMutableSet()
                        planValidating.conditions.clear()
                        planValidating.conditions.addAll(tempConditions)
                    }

                    validateIdNotEmpty("Проверка, что заголовок не пуст")
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validatePriceNotEmpty("Проверка, что цена не пуста")
                    validateConditionsNotEmpty("Проверка, что условия не пусты")

                    validateIdProperFormat("Проверка формата id")
                    validateTitleHasContent("Проверка присутствия текста в заголовке")
                    validateDurationIsPositive("Проверка позитивного значения длительности плана")
                    validatePriceProperFormat("Проверка формата цены подписки")
                    validateConditions("Проверка условий подписки")


                    finishPlanValidation("Завершение валидации")
                }
            }

            operation("Чтение плана", PlanCommand.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }

                validation {
                    worker("Копируем поля в PlanValidating") { planValidating = planRequest.deepCopy() }
                    worker("Очистка id") { planValidating.id = PlanId(planValidating.id.asString().trim()) }

                    validateIdNotEmpty("Проверка, что заголовок не пуст")
                    validateIdProperFormat("Проверка формата id")

                    finishPlanValidation("Завершение валидации")
                }
            }

            operation("Чтение всех планов", PlanCommand.READ_ALL) {
                stubs("Обработка стабов") {
                    stubReadAllSuccess("Имитация успешной обработки")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }

            operation("Удаление плана", PlanCommand.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }

                validation {
                    worker("Копируем поля в PlanValidating") { planValidating = planRequest.deepCopy() }
                    worker("Очистка id") { planValidating.id = PlanId(planValidating.id.asString().trim()) }

                    validateIdNotEmpty("Проверка, что заголовок не пуст")
                    validateIdProperFormat("Проверка формата id")

                    finishPlanValidation("Завершение валидации")
                }
            }
        }.build()
    }
}
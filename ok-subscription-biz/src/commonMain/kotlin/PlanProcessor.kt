import contexts.PlanContext
import dsl.rootChain
import general.operation
import models.plan.PlanCommand
import stubs.*
import stubs.plan.*

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
            }

            operation("Чтение плана", PlanCommand.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
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
            }
        }.build()
    }
}
import contexts.PlanContext
import dsl.chain
import dsl.rootChain
import dsl.worker
import general.operation
import general.plan.prepareResult
import models.RepoSettings
import models.SbscrState
import models.plan.PlanCommand
import models.plan.PlanId
import models.plan.PlanLock
import permissions.accessPlanValidation
import permissions.chainPlanPermissions
import permissions.frontPlanPermissions
import permissions.searchPlanTypes
import repo.plan.*
import stubs.*
import stubs.plan.*
import validation.finishPlanValidation
import validation.plan.*
import validation.validation

class PlanProcessor(private val repoSettings: RepoSettings = RepoSettings()) {
    suspend fun exec(ctx: PlanContext) = PlanChain.exec(ctx.apply { this.planRepoSettings = repoSettings.planRepoSettings })

    companion object {
        @Suppress("DuplicatedCode")
        private val PlanChain = rootChain<PlanContext> {
            initStatus("Инициализация цепи")
            initPlanRepo("Инициализация репозитория")

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
                chainPlanPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика сохранения"
                    repoPrepareCreate("Подготовка объекта для сохранения")
                    accessPlanValidation("Вычисление прав доступа")
                    repoCreate("Создание объявления в БД")
                }
                frontPlanPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка результата")
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
                    worker("Очистка lock") { planValidating.lock = PlanLock(planValidating.lock.asString().trim()) }
                    worker("Очистка названия плана") { planValidating.title = planValidating.title.trim() }
                    worker("Очистка цены") { planValidating.price = planValidating.price.trim() }
                    worker("Очистка условий подписки") {
                        val tempConditions = planValidating.conditions.map { it.trim() }.toMutableSet()
                        planValidating.conditions.clear()
                        planValidating.conditions.addAll(tempConditions)
                    }

                    validateIdNotEmpty("Проверка, что заголовок не пуст")
                    validateLockNotEmpty("Проверка, что замок не пуст")
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validatePriceNotEmpty("Проверка, что цена не пуста")
                    validateConditionsNotEmpty("Проверка, что условия не пусты")

                    validateIdProperFormat("Проверка формата id")
                    validateLockProperFormat("Проверка формата замка")
                    validateTitleHasContent("Проверка присутствия текста в заголовке")
                    validateDurationIsPositive("Проверка позитивного значения длительности плана")
                    validatePriceProperFormat("Проверка формата цены подписки")
                    validateConditions("Проверка условий подписки")

                    finishPlanValidation("Завершение валидации")
                }

                chainPlanPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика обновления"
                    repoRead("Чтение плана из БД")
                    accessPlanValidation("Вычисление прав доступа")
                    repoPrepareUpdate("Подготовка объекта для обновления")
                    repoUpdate("Обновление плана в БД")
                }
                frontPlanPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
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

                chainPlanPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика чтения"
                    repoRead("Чтение плана из БД")
                    accessPlanValidation("Вычисление прав доступа")
                    worker {
                        title = "Подготовка ответа для read"
                        on { state == SbscrState.RUNNING }
                        handle { planRepoDone = planRepoRead }
                    }
                }
                frontPlanPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }

            operation("Чтение всех планов", PlanCommand.READ_ALL) {
                stubs("Обработка стабов") {
                    stubReadAllSuccess("Имитация успешной обработки")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }

                chainPlanPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика чтения всех планов"
                    searchPlanTypes("Добавление фильтра групп доступа")
                    repoPlanSearch("Чтение всех планов из БД")
                    worker {
                        title = "Подготовка ответа для readAll"
                        on { state == SbscrState.RUNNING }
                        handle { plansRepoDone = planRepoReadAll }
                    }
                }
                frontPlanPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
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
                    worker("Очистка lock") { planValidating.lock = PlanLock(planValidating.lock.asString().trim()) }

                    validateIdNotEmpty("Проверка, что заголовок не пуст")
                    validateLockNotEmpty("Проверка, что замок не пуст")
                    validateIdProperFormat("Проверка формата id")
                    validateLockProperFormat("Проверка формата замка")

                    finishPlanValidation("Завершение валидации")
                }
                chainPlanPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика удаления"
                    repoRead("Чтение плана из БД")
                    accessPlanValidation("Вычисление прав доступа")
                    repoPrepareDelete("Подготовка объекта плана для удаления")
                    repoDelete("Удаление плана из БД")
                }
                frontPlanPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }
        }.build()
    }
}
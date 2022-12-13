import contexts.SubscriptionContext
import dsl.chain
import dsl.rootChain
import dsl.worker
import general.operation
import general.subscription.prepareResult
import models.RepoSettings
import models.SbscrState
import models.SbscrUserId
import models.plan.PlanId
import models.subscription.SubscriptionCommand
import models.subscription.SubscriptionId
import permissions.accessValidation
import permissions.chainPermissions
import permissions.searchTypes
import repo.subscription.*
import stubs.*
import stubs.subscription.*
import validation.finishSubscriptionFilterValidation
import validation.finishSubscriptionValidation
import validation.subscription.validateIdNotEmpty
import validation.subscription.validateIdProperFormat
import validation.subscription.validatePlanIdNotEmpty
import validation.subscription.validatePlanIdProperFormat
import validation.validation

class SubscriptionProcessor(private val processorRepoSettings: RepoSettings = RepoSettings()) {
    suspend fun exec(ctx: SubscriptionContext) = SubscriptionChain.exec(ctx.apply {
        this.repoSettings = processorRepoSettings
    })

    companion object {
        @Suppress("DuplicatedCode")
        private val SubscriptionChain = rootChain<SubscriptionContext> {
            initStatus("Инициализация цепи")
            initRepo("Инициализация репозиториев")

            operation("Приобретение подписки", SubscriptionCommand.BUY) {
                stubs("Обработка стабов"){
                    stubBuySuccess("Имитация успешного приобретения подписки")
                    stubCannotBuy("Имитация ошибки приобретения подписки")
                    stubValidationBadId("Имитация ошибки валидации id плана")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }

                validation {
                    worker("Копируем поля в SubscriptionValidating") { subscriptionValidating = subscriptionRequest.deepCopy() }
                    worker("Очистка planId") {
                        subscriptionValidating.planId = PlanId(subscriptionValidating.planId.asString().trim())
                    }

                    validatePlanIdNotEmpty("Проверка, что planId не пуст")
                    validatePlanIdProperFormat("Проверка формата planId")

                    finishSubscriptionValidation("Завершение валидации")
                }

                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика сохранения"
                    repoPrepareCreate("Подготовка подписки для сохранения")
                    accessValidation("Проверка прав доступа")
                    repoCreate("Создание подписки в БД")
                }
                prepareResult("Подготовка результата")
            }

            operation("Оплата подписки", SubscriptionCommand.PAY) {
                stubs("Обработка стабов"){
                    stubPaySuccess("Имитация успешной оплаты подписки")
                    stubValidationBadId("Имитация ошибки валидации id подписки")
                    stubPaymentError("Имитация неуспешной оплаты, либо ошибки оплаты")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }

                validation {
                    worker("Копируем поля в SubscriptionValidating") { subscriptionValidating = subscriptionRequest.deepCopy() }
                    worker("Очистка Id") {
                        subscriptionValidating.id = SubscriptionId(subscriptionValidating.id.asString().trim())
                    }

                    validateIdNotEmpty("Проверка, что Id не пуст")
                    validateIdProperFormat("Проверка формата Id")

                    finishSubscriptionValidation("Завершение валидации")
                }

                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика обновления"
                    repoRead("Чтение подписки из БД")
                    repoReadPlan("Чтение плана подписки")
                    accessValidation("Проверка прав доступа")
                    repoPreparePaymentAndDates("Подготовка объекта для обновления")
                    repoUpdate("Обновление подписки в БД")
                }
                prepareResult("Подготовка ответа")
            }

            operation("Чтение подписки", SubscriptionCommand.READ) {
                stubs("Обработка стабов"){
                    stubReadSuccess("Имитация чтения подписки")
                    stubValidationBadId("Имитация ошибки валидации id подписки")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }

                validation {
                    worker("Копируем поля в SubscriptionValidating") { subscriptionValidating = subscriptionRequest.deepCopy() }
                    worker("Очистка Id") {
                        subscriptionValidating.id = SubscriptionId(subscriptionValidating.id.asString().trim())
                    }

                    validateIdNotEmpty("Проверка, что Id не пуст")
                    validateIdProperFormat("Проверка формата Id")

                    finishSubscriptionValidation("Завершение валидации")
                }

                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика чтения"
                    repoRead("Чтение подписки из БД")
                    accessValidation("Проверка прав доступа")
                    worker {
                        title = "Подготовка ответа для read"
                        on { state == SbscrState.RUNNING }
                        handle { subscriptionRepoDone = subscriptionRepoRead }
                    }
                }
                prepareResult("Подготовка ответа")
            }

            operation("Поиск приобретенных подписок", SubscriptionCommand.SEARCH) {
                stubs("Обработка стабов"){
                    stubSearchSuccess("Имитация успешного поиска подписки")
                    stubValidationBadId("Имитация ошибки валидации id подписки")
                    stubValidationBadSearchParameters("Имитация ошибки валидации фильтра подписок")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }


                validation {
                    worker("Копируем поля в SubscriptionFilterValidating") { subscriptionFilterValidating = subscriptionFilter.deepCopy() }
                    worker("Очистка ownerId и planId") {
                        subscriptionValidating.ownerId = SbscrUserId(subscriptionValidating.ownerId.asString().trim())
                        subscriptionValidating.planId = PlanId(subscriptionValidating.planId.asString().trim())
                    }

                    finishSubscriptionFilterValidation("Завершение валидации")
                }

                chainPermissions("Вычисление разрешений для пользователя")
                searchTypes("Подготовка поискового запроса")

                repoSearch("Поиск подписок по фильтру")
                prepareResult("Подготовка ответа")
            }
        }.build()
    }
}
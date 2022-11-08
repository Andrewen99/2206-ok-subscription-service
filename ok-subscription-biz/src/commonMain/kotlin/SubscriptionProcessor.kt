import contexts.SubscriptionContext
import dsl.rootChain
import dsl.worker
import general.operation
import models.SbscrUserId
import models.plan.PlanId
import models.subscription.SubscriptionCommand
import models.subscription.SubscriptionId
import models.subscription.SubscriptionRepoSettings
import stubs.*
import stubs.subscription.*
import validation.finishSubscriptionFilterValidation
import validation.finishSubscriptionValidation
import validation.subscription.validateIdNotEmpty
import validation.subscription.validateIdProperFormat
import validation.subscription.validatePlanIdNotEmpty
import validation.subscription.validatePlanIdProperFormat
import validation.validation

class SubscriptionProcessor(private val repoSettings: SubscriptionRepoSettings = SubscriptionRepoSettings()) {
    suspend fun exec(ctx: SubscriptionContext) = SubscriptionChain.exec(ctx.apply { this.subscriptionRepoSettings = repoSettings })

    companion object {
        @Suppress("DuplicatedCode")
        private val SubscriptionChain = rootChain<SubscriptionContext> {
            initStatus("Инициализация цепи")
//            initSubscriptionRepo("Инициализация репозитория")

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
            }
        }.build()
    }
}
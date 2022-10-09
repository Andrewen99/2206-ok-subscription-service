import contexts.SubscriptionContext
import dsl.rootChain
import general.operation
import models.subscription.SubscriptionCommand
import stubs.*
import stubs.subscription.*

class SubscriptionProcessor {
    suspend fun exec(ctx: SubscriptionContext) = SubscriptionChain.exec(ctx)

    companion object {
        private val SubscriptionChain = rootChain<SubscriptionContext> {
            initStatus("Инициализация цепи")

            operation("Приобретение подписки", SubscriptionCommand.BUY) {
                stubs("Обработка стабов"){
                    stubBuySuccess("Имитация успешного приобретения подписки")
                    stubCannotBuy("Имитация ошибки приобретения подписки")
                    stubValidationBadId("Имитация ошибки валидации id плана")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
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
            }

            operation("Чтение подписки", SubscriptionCommand.READ) {
                stubs("Обработка стабов"){
                    stubReadSuccess("Имитация чтения подписки")
                    stubValidationBadId("Имитация ошибки валидации id подписки")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
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
            }
        }.build()
    }
}
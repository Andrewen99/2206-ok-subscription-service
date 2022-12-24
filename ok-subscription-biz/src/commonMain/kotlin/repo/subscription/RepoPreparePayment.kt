package repo.subscription

import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import models.SbscrState
import models.subscription.SbscrPaymentStatus
import util.getTodayAsLocalDate

fun CorChainDsl<SubscriptionContext>.repoPreparePaymentAndDates(title: String) = worker {
    this.title = title
    description = "Готовим подписку для обновления статуса оплаты в БД"
    on { state == SbscrState.RUNNING }
    handle {
        subscriptionRepoPrepare = subscriptionRepoRead.deepCopy().apply {
            this.paymentStatus = SbscrPaymentStatus.PAYED
            this.isActive = true
            this.startDate = getTodayAsLocalDate()
            this.endDate = this.startDate.plus(planRepoRead.duration, DateTimeUnit.MONTH)
        }
    }
}
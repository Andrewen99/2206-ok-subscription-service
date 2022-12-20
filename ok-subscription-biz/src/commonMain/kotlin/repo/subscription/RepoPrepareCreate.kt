package repo.subscription

import contexts.PlanContext
import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState


fun CorChainDsl<SubscriptionContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в БД"
    on { state == SbscrState.RUNNING }
    handle {
        subscriptionRepoRead = subscriptionValidated.deepCopy()
        subscriptionRepoRead.ownerId = principal.id
        subscriptionRepoPrepare = subscriptionRepoRead
    }
}
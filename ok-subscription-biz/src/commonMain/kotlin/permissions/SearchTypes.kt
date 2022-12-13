package permissions

import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.chain
import dsl.worker
import models.SbscrState
import models.subscription.SearchPermissions

fun CorChainDsl<SubscriptionContext>.searchTypes(title: String) = chain {
    this.title = title
    description = "Добавление ограничений в поисковый запрос согласно правам доступа и др. политикам"
    on { state == SbscrState.RUNNING }
    worker("Определение типа поиска") {
        subscriptionFilterValidated.searchPermissions = setOfNotNull(
            SearchPermissions.OWN.takeIf { permissionsChain.contains(UserSubscriptionPermissions.SEARCH_OWN) },
            SearchPermissions.ALL.takeIf { permissionsChain.contains(UserSubscriptionPermissions.SEARCH) }
        ).toMutableSet()
    }
}
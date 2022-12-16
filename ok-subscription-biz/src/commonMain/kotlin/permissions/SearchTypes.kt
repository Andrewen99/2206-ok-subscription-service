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
            SearchPermissions.ALL.takeIf { permissionsChain.contains(UserSubscriptionPermissions.SEARCH) }
        ).toMutableSet()
        if (permissionsChain.contains(UserSubscriptionPermissions.SEARCH_OWN) && !permissionsChain.contains(UserSubscriptionPermissions.SEARCH)) {
            subscriptionFilterValidated.ownerId = principal.id
            subscriptionFilterValidated.searchPermissions.add(SearchPermissions.OWN)
        }
    }
}
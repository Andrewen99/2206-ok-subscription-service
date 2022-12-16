package permissions

import contexts.PlanContext
import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.chain
import dsl.worker
import models.SbscrState
import models.plan.SbscrPlanVisibility
import models.subscription.SearchPermissions

fun CorChainDsl<SubscriptionContext>.searchSubscriptionTypes(title: String) = chain {
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

fun CorChainDsl<PlanContext>.searchPlanTypes(title: String) = chain {
    this.title = title
    description = "Добавление ограничений в поисковый запрос согласно правам доступа и др. политикам"
    on { state == SbscrState.RUNNING }
    worker("Определение типа поиска") {
        planFilterValidated.visibilitySet = if (principal.groups.contains(SbscrUserGroups.ADMIN))
            setOf(SbscrPlanVisibility.PUBLIC, SbscrPlanVisibility.ADMIN_ONLY)
        else
            setOf(SbscrPlanVisibility.PUBLIC)
    }
}
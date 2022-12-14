package permissions

import contexts.PlanContext
import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import plan.resolvePlanChainPermissions
import subscription.resolveSubscriptionChainPermissions

fun CorChainDsl<PlanContext>.chainPlanPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление прав доступа к планам для групп пользователей"

    handle {
        permissionsChain.addAll(resolvePlanChainPermissions(principal.groups))
        println("PRINCIPAL: $principal")
        println("PERMISSIONS: $permissionsChain")
    }
}

fun CorChainDsl<SubscriptionContext>.chainSubscriptionPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление прав доступа к планам для групп пользователей"

    handle {
        permissionsChain.addAll(resolveSubscriptionChainPermissions(principal.groups))
    }
}
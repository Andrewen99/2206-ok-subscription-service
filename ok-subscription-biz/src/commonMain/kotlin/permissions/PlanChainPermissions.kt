package permissions

import contexts.PlanContext
import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import resolvePlanChainPermissions
import resolveSubscriptionChainPermissions

fun CorChainDsl<PlanContext>.chainPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление прав доступа к планам для групп пользователей"

    handle {
        permissionsChain.addAll(resolvePlanChainPermissions(principal.groups))
        println("PRINCIPAL: $principal")
        println("PERMISSIONS: $permissionsChain")
    }
}

fun CorChainDsl<SubscriptionContext>.chainPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление прав доступа к планам для групп пользователей"

    handle {
        permissionsChain.addAll(resolveSubscriptionChainPermissions(principal.groups))
    }
}
package permissions

import contexts.PlanContext
import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import models.SbscrState
import plan.resolvePlanFrontPermissions
import resolveRelationsTo
import subscription.resolveSubscriptionFrontPermissions

fun CorChainDsl<PlanContext>.frontPlanPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { state == SbscrState.RUNNING }

    handle {
        planRepoDone.permissionsClient.addAll(
            resolvePlanFrontPermissions(
                permissionsChain,
                // Повторно вычисляем отношения, поскольку они могли измениться при выполении операции
                planRepoDone.resolveRelationsTo(principal)
            )
        )

        for (plan in plansRepoDone) {
            plan.permissionsClient.addAll(
                resolvePlanFrontPermissions(
                    permissionsChain,
                    plan.resolveRelationsTo(principal)
                )
            )
        }
    }
}

fun CorChainDsl<SubscriptionContext>.frontSubscriptionPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { state == SbscrState.RUNNING }

    handle {
        subscriptionRepoDone.permissionsClient.addAll(
            resolveSubscriptionFrontPermissions(
                permissionsChain,
                // Повторно вычисляем отношения, поскольку они могли измениться при выполении операции
                subscriptionRepoDone.resolveRelationsTo(principal)
            )
        )

        for (subscription in subscriptionsRepoDone) {
            subscription.permissionsClient.addAll(
                resolveSubscriptionFrontPermissions(
                    permissionsChain,
                    subscription.resolveRelationsTo(principal)
                )
            )
        }
    }
}
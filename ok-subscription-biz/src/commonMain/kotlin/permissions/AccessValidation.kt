package permissions

import plan.checkPlanPermitted
import subscription.checkSubscriptionPermitted
import contexts.PlanContext
import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.chain
import dsl.worker
import helpers.fail
import models.SbscrError
import models.SbscrState
import resolveRelationsTo

fun CorChainDsl<PlanContext>.accessPlanValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { state == SbscrState.RUNNING }
    worker("Вычисление отношения Плана к принципалу") {
        planRepoRead.principalRelations = planRepoRead.resolveRelationsTo(principal)
    }
    worker("Вычисление доступа к плану") {
        permitted = checkPlanPermitted(command, planRepoRead.principalRelations, permissionsChain)
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            fail(SbscrError(message = "User is not allowed to perform this operation on plan"))
        }
    }
}

fun CorChainDsl<SubscriptionContext>.accessSubscriptionValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { state == SbscrState.RUNNING }
    worker("Вычисление отношения плана к принципалу") {
     subscriptionRepoRead.principalRelations = subscriptionRepoRead.resolveRelationsTo(principal)
    }
    worker("Вычисление доступа к плану") {
        permitted = checkSubscriptionPermitted(command, subscriptionRepoRead.principalRelations, permissionsChain)
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            fail(SbscrError(message = "User is not allowed to perform this operation on subscription"))
        }
    }
}
package stubs

import contexts.PlanContext
import contexts.SubscriptionContext
import dsl.CorChainDsl
import dsl.worker
import helpers.errorAdministration
import helpers.fail
import models.SbscrWorkMode
import repo.plan.IPlanRepository

fun CorChainDsl<PlanContext>.initPlanRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        planRepo = when (workMode) {
            SbscrWorkMode.TEST -> planRepoSettings.repoTest
            SbscrWorkMode.STUB -> planRepoSettings.repoStub
            else -> planRepoSettings.repoProd
        }
        if (workMode != SbscrWorkMode.STUB && planRepo == IPlanRepository.NONE) fail(
            errorAdministration(
                field = "plan-repo",
                violationCode = "dbNotConfigured",
                description = "The database is unconfigured for chosen workmode ($workMode). " +
                        "Please, contact the administrator staff"
            )
        )
    }
}

fun CorChainDsl<SubscriptionContext>.initSubscriptionRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        subscriptionRepo = when (workMode) {
            SbscrWorkMode.TEST -> subscriptionRepoSettings.repoTest
            SbscrWorkMode.STUB -> subscriptionRepoSettings.repoStub
            else -> subscriptionRepoSettings.repoProd
        }
        if (workMode != SbscrWorkMode.STUB && subscriptionRepo == IPlanRepository.NONE) fail(
            errorAdministration(
                field = "plan-repo",
                violationCode = "dbNotConfigured",
                description = "The database is unconfigured for chosen workmode ($workMode). " +
                        "Please, contact the administrator staff"
            )
        )
    }
}
package models.plan

import repo.plan.IPlanRepository

data class PlanRepoSettings(
    val repoStub: IPlanRepository = IPlanRepository.NONE,
    val repoTest: IPlanRepository = IPlanRepository.NONE,
    val repoProd: IPlanRepository = IPlanRepository.NONE,
)
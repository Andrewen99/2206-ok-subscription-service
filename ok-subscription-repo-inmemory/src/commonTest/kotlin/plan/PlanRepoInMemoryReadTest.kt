package plan

import repo.plan.IPlanRepository

class PlanRepoInMemoryReadTest : RepoPlanReadTest() {
    override val repo = PlanRepoInMemory(
        initObjects = initObjects,
    )
}
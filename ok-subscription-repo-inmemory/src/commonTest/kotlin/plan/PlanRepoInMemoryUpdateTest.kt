package plan

import repo.plan.IPlanRepository

class PlanRepoInMemoryUpdateTest : RepoPlanUpdateTest() {
    override val repo = PlanRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}
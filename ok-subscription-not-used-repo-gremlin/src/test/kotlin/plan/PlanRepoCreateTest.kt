package plan

import PlanRepoGremlin
import repo.plan.IPlanRepository

class PlanRepoCreateTest : RepoPlanCreateTest() {
    override val repo: IPlanRepository by lazy {
        PlanRepoGremlin(
            hosts = "0.0.0.0",
            port = 2480,
            enableSsl = false,
            initObjects = initObjects,
            initRepo = { g -> g.V().drop().iterate() },
            randomUuid = { lockNew.asString() }
        )
    }
}
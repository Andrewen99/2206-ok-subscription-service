import plan.*
import repo.plan.IPlanRepository


class RepoPlanSqlCreateTest : RepoPlanCreateTest() {
    override val repo: IPlanRepository = SqlTestCompanion.planRepoUnderTestContainer(
        randomUuid = { lockNew.asString() },
    )
}

class RepoPlanSqlUpdateTest : RepoPlanUpdateTest() {
    override val repo: IPlanRepository = SqlTestCompanion.planRepoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}

class RepoPlanSqlReadTest : RepoPlanReadTest() {
    override val repo: IPlanRepository = SqlTestCompanion.planRepoUnderTestContainer(initObjects)
}

class RepoPlanSqlSearchTest : RepoPlanSearchTest() {
    override val repo: IPlanRepository = SqlTestCompanion.planRepoUnderTestContainer(initObjects)
}

class RepoPlanSqlDeleteTest : RepoPlanDeleteTest() {
    override val repo: IPlanRepository = SqlTestCompanion.planRepoUnderTestContainer(initObjects)
}
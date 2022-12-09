package plan

class PlanRepoInMemoryCreateTest : RepoPlanCreateTest() {
    override val repo = PlanRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}
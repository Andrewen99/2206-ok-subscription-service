package plan

class PlanRepoInMemoryDeleteTest: RepoPlanDeleteTest() {
    override val repo = PlanRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}
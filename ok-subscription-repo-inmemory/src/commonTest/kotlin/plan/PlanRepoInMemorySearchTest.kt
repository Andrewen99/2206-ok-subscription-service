package plan

class PlanRepoInMemorySearchTest: RepoPlanSearchTest() {
    override val repo = PlanRepoInMemory(
        initObjects = initObjects
    )
}
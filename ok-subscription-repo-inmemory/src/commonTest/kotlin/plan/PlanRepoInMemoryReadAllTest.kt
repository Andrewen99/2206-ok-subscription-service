package plan

class PlanRepoInMemoryReadAllTest: RepoPlanReadAllTest() {
    override val repo = PlanRepoInMemory(
        initObjects = initObjects
    )
}
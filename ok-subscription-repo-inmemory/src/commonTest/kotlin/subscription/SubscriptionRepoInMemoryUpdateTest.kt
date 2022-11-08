package subscription

class SubscriptionRepoInMemoryUpdateTest : RepoSubscriptionUpdateTest() {
    override val repo = SubscriptionRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}
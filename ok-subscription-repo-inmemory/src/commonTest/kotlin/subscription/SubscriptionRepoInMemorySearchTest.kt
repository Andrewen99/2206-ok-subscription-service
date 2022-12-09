package subscription

class SubscriptionRepoInMemorySearchTest : RepoSubscriptionSearchTest() {
    override val repo = SubscriptionRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}
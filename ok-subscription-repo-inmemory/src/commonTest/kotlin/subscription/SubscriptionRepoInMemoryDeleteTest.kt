package subscription

class SubscriptionRepoInMemoryDeleteTest : RepoSubscriptionDeleteTest() {
    override val repo = SubscriptionRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}
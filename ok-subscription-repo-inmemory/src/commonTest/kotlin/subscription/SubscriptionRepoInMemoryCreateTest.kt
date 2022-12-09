package subscription

class SubscriptionRepoInMemoryCreateTest: RepoSubscriptionCreateTest() {
    override val repo = SubscriptionRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}
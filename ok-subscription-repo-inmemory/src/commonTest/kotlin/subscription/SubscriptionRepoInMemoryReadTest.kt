package subscription

class SubscriptionRepoInMemoryReadTest : RepoSubscriptionReadTest() {
    override val repo = SubscriptionRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}
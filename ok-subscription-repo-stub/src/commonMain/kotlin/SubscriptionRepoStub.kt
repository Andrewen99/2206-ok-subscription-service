import repo.subscription.*

class SubscriptionRepoStub : ISubscriptionRepository {
    override suspend fun createSubscription(rq: DbSubscriptionRequest): DbSubscriptionResponse {
        return DbSubscriptionResponse(
            data = SubscriptionStubs.SUBSCRIPTION1,
            success = true
        )
    }

    override suspend fun readSubscription(rq: DbSubscriptionIdRequest): DbSubscriptionResponse {
        return DbSubscriptionResponse(
            data = SubscriptionStubs.SUBSCRIPTION1,
            success = true
        )
    }

    override suspend fun updateSubscription(rq: DbSubscriptionRequest): DbSubscriptionResponse {
        return DbSubscriptionResponse(
            data = SubscriptionStubs.SUBSCRIPTION1,
            success = true
        )
    }

    override suspend fun deleteSubscription(rq: DbSubscriptionIdRequest): DbSubscriptionResponse {
        return DbSubscriptionResponse(
            data = SubscriptionStubs.SUBSCRIPTION1,
            success = true
        )
    }

    override suspend fun searchSubscription(rq: DbSubscriptionFilterRequest): DbSubscriptionsResponse {
        return DbSubscriptionsResponse(
            data = SubscriptionStubs.SUBSCRIPTIONS,
            success = true
        )
    }
}
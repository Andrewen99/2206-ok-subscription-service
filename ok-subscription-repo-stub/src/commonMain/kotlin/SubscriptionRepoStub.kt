import models.SbscrUserId
import repo.subscription.*

class SubscriptionRepoStub(private val ownerId: SbscrUserId = SbscrUserId("owner-id-1")) : ISubscriptionRepository {
    override suspend fun createSubscription(rq: DbSubscriptionRequest): DbSubscriptionResponse {
        return DbSubscriptionResponse(
            data = SubscriptionStubs.SUBSCRIPTION1.also { it.ownerId = ownerId },
            success = true
        )
    }

    override suspend fun readSubscription(rq: DbSubscriptionIdRequest): DbSubscriptionResponse {
        return DbSubscriptionResponse(
            data = SubscriptionStubs.SUBSCRIPTION1.also { it.ownerId = ownerId },
            success = true
        )
    }

    override suspend fun updateSubscription(rq: DbSubscriptionRequest): DbSubscriptionResponse {
        return DbSubscriptionResponse(
            data = SubscriptionStubs.SUBSCRIPTION1.also { it.ownerId = ownerId },
            success = true
        )
    }

    override suspend fun deleteSubscription(rq: DbSubscriptionIdRequest): DbSubscriptionResponse {
        return DbSubscriptionResponse(
            data = SubscriptionStubs.SUBSCRIPTION1.also { it.ownerId = ownerId },
            success = true
        )
    }

    override suspend fun searchSubscription(rq: DbSubscriptionFilterRequest): DbSubscriptionsResponse {
        return DbSubscriptionsResponse(
            data = SubscriptionStubs.SUBSCRIPTIONS.map {
                it.ownerId = ownerId
                it
            },
            success = true
        )
    }
}
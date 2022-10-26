package repo.subscription

interface ISubscriptionRepository {

    suspend fun createSubscription(rq: DbSubscriptionRequest) : DbSubscriptionResponse

    suspend fun readSubscription(rq: DbSubscriptionIdRequest) : DbSubscriptionResponse

    suspend fun updateSubscription(rq: DbSubscriptionRequest) : DbSubscriptionResponse

    suspend fun deleteSubscription(rq: DbSubscriptionIdRequest) : DbSubscriptionResponse

    suspend fun searchSubscription(rq: DbSubscriptionFilterRequest) : DbSubscriptionsResponse

    companion object {
        val NONE = object : ISubscriptionRepository {
            override suspend fun createSubscription(rq: DbSubscriptionRequest): DbSubscriptionResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readSubscription(rq: DbSubscriptionIdRequest): DbSubscriptionResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateSubscription(rq: DbSubscriptionRequest): DbSubscriptionResponse {
                TODO("Not yet implemented")
            }

            override suspend fun deleteSubscription(rq: DbSubscriptionIdRequest): DbSubscriptionResponse {
                TODO("Not yet implemented")
            }

            override suspend fun searchSubscription(rq: DbSubscriptionFilterRequest): DbSubscriptionsResponse {
                TODO("Not yet implemented")
            }

        }
    }

}
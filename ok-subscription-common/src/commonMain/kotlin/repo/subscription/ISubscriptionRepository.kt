package repo.subscription

import exceptions.DbDuplicatedElementsException
import helpers.errorAdministration
import helpers.errorRepoConcurrency
import models.SbscrError
import models.subscription.Subscription

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

        val resultErrorEmptyId = DbSubscriptionResponse(
            data = null,
            success = false,
            errors = listOf(
                SbscrError(
                    code = "id-empty",
                    group = "validation",
                    field = "id",
                    message = "Id must not be null or blank",
                )
            )
        )

        val resultErrorEmptyLock = DbSubscriptionResponse(
            data = null,
            success = false,
            errors = listOf(
                SbscrError(
                    code = "lock-empty",
                    group = "validation",
                    field = "lock",
                    message = "Lock must not be null or blank",
                )
            )
        )

        fun resultErrorNotFound(key: String) = DbSubscriptionResponse(
            data = null,
            success = false,
            errors = listOf(
                SbscrError(
                    code = "not-found",
                    field = "id",
                    group = "repo",
                    message = "Not Found object with id $key",
                )
            )
        )

        fun errorDuplication(key: String) = DbSubscriptionResponse(
            data = null,
            success = false,
            errors = listOf(
                errorAdministration(
                    violationCode = "duplicateObjects",
                    description = "Database consistency failure",
                    exception = DbDuplicatedElementsException("Db contains multiple elements with id = '$key'")
                )
            )
        )

        fun resultErrorConcurrent(lock: String, subscription: Subscription?) = DbSubscriptionResponse(
            data = subscription,
            success = false,
            errors = listOf(
                errorRepoConcurrency(lock, subscription?.lock?.asString())
            )
        )
    }

}
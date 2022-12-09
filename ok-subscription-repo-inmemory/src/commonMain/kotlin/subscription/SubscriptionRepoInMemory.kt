package subscription

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import helpers.errorRepoConcurrency
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import models.SbscrError
import models.SbscrUserId
import models.plan.PlanId
import models.plan.PlanLock
import models.subscription.Subscription
import models.subscription.SubscriptionId
import models.subscription.SubscriptionLock
import repo.plan.DbPlanResponse
import repo.subscription.*
import util.inPeriod
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class SubscriptionRepoInMemory(
    initObjects: List<Subscription> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() }
) : ISubscriptionRepository {
    /**
     * Инициализация кеша с установкой "времени жизни" данных после записи
     */
    private val cache = Cache.Builder()
        .expireAfterWrite(ttl)
        .build<String, SubscriptionEntity>()
    private val mutex: Mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(subscription: Subscription) {
        val entity = SubscriptionEntity(subscription)
        if (entity.id == null)
            return
        cache.put(entity.id, entity)
    }
    override suspend fun createSubscription(rq: DbSubscriptionRequest): DbSubscriptionResponse {
        val key = randomUuid()
        val subscription = rq.subscription.copy(id = SubscriptionId(key), lock = SubscriptionLock(key))
        val entity = SubscriptionEntity(subscription)
        cache.put(key, entity)
        return DbSubscriptionResponse(
            data = subscription,
            success = true
        )
    }

    override suspend fun readSubscription(rq: DbSubscriptionIdRequest): DbSubscriptionResponse {
        val key = rq.id.takeIf { it != SubscriptionId.NONE }?.asString() ?: return resultErrorEmptyId
        return cache.get(key)
            ?.let {
                DbSubscriptionResponse(
                    data = it.toInternal(),
                    success = true
                )
            } ?: resultErrorNotFound
    }

    override suspend fun updateSubscription(rq: DbSubscriptionRequest): DbSubscriptionResponse {
        val key = rq.subscription.id.takeIf { it != SubscriptionId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.subscription.lock.takeIf { it != SubscriptionLock.NONE }?.asString() ?: return resultErrorEmptyLock
        val newSubscription = rq.subscription.copy(lock = SubscriptionLock(randomUuid()))
        val entity = SubscriptionEntity(newSubscription)
        return mutex.withLock {
            val oldSubscription = cache.get(key)
            when {
                oldSubscription == null -> resultErrorNotFound
                oldSubscription.lock != oldLock  -> DbSubscriptionResponse(
                    data = oldSubscription.toInternal(),
                    success = false,
                    errors = listOf(errorRepoConcurrency(oldLock, oldSubscription.lock))
                )

                else -> {
                    cache.put(key, entity)
                    return DbSubscriptionResponse(
                        data = newSubscription,
                        success = true
                    )
                }
            }
        }
    }

    override suspend fun deleteSubscription(rq: DbSubscriptionIdRequest): DbSubscriptionResponse {
        val key = rq.id.takeIf { it != SubscriptionId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.lock.takeIf { it != SubscriptionLock.NONE }?.asString() ?: return resultErrorEmptyLock
        return mutex.withLock {
            val oldSubscription = cache.get(key)
            when {
                oldSubscription == null -> return resultErrorNotFound
                oldSubscription.lock != oldLock -> return DbSubscriptionResponse(
                    data = oldSubscription.toInternal(),
                    success = false,
                    errors = listOf(errorRepoConcurrency(oldLock, oldSubscription.lock))
                )

                else -> {
                    cache.invalidate(key)
                    return DbSubscriptionResponse(
                        data = oldSubscription.toInternal(),
                        success = true
                    )
                }
            }
        }
    }

    override suspend fun searchSubscription(rq: DbSubscriptionFilterRequest): DbSubscriptionsResponse {
        val rqFilter = rq.filter
        val result = cache.asMap().asSequence()
            .filter { entry ->
                rqFilter.ownerId.takeIf { it != SbscrUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                rqFilter.planId.takeIf { it != PlanId.NONE }?.let {
                    it.asString() == entry.value.planId
                } ?: true
            }
            .filter { entry ->
                rqFilter.subscriptionId.takeIf { it != SubscriptionId.NONE }?.let {
                    it.asString() == entry.value.id
                } ?: true
            }
            .filter { entry ->
                rqFilter.boughtPeriod?.let {
                    entry.value.startDate?.inPeriod(it)
                } ?: true
            }
            .filter { entry ->
                rqFilter.expirationPeriod?.let {
                    entry.value.endDate?.inPeriod(it)
                } ?: true
            }
            .filter { entry ->
                rqFilter.isActive?.let {
                    it == entry.value.isActive
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        return DbSubscriptionsResponse(
            data = result,
            success = true
        )
    }

    companion object {
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

        val resultErrorNotFound = DbSubscriptionResponse(
            data = null,
            success = false,
            errors = listOf(
                SbscrError(
                    code = "not-found",
                    field = "id",
                    group = "repo",
                    message = "Not Found",
                )
            )
        )
    }
}
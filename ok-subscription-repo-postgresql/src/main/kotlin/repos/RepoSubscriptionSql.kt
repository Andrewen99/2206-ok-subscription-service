package repos

import SqlConnector
import com.benasher44.uuid.uuid4
import models.SbscrError
import models.SbscrUserId
import models.plan.PlanId
import models.subscription.Subscription
import models.subscription.SubscriptionId
import models.subscription.SubscriptionLock
import org.jetbrains.exposed.sql.*
import repo.subscription.*
import repo.subscription.ISubscriptionRepository.Companion.errorDuplication
import repo.subscription.ISubscriptionRepository.Companion.resultErrorConcurrent
import repo.subscription.ISubscriptionRepository.Companion.resultErrorNotFound
import tables.PlansTable
import tables.SubscriptionsTable
import tables.UsersTable
import util.MIN_LOCAL_DATE
import java.lang.IllegalArgumentException
import java.util.NoSuchElementException

class RepoSubscriptionSql(
    private val db: Database,
    initObjects: Collection<Subscription> = emptyList(),
    val randomUuid: () -> String = { uuid4().toString() }
) : ISubscriptionRepository {

    init {
        initObjects.forEach {
            save(it)
        }
    }

    constructor(
        url: String = "jdbc:postgresql://localhost:5432/sbscrdevdb",
        user: String = "postgres",
        password: String = "sbscr-pass",
        schema: String = "sbscr",
        initObjects: Collection<Subscription> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() }
    ) : this(SqlConnector(url, user, password, schema).connect(tables = arrayOf(PlansTable, SubscriptionsTable, UsersTable)), initObjects, randomUuid)

    private fun save(item: Subscription): DbSubscriptionResponse {
        return safeTransaction(db, {
            val res = SubscriptionsTable.insert {
                if (item.id != SubscriptionId.NONE) {
                    it[id] = item.id.asString()
                } else {
                    it[PlansTable.id] = randomUuid()
                }
                it[ownerId] = item.ownerId.asString()
                it[planId] = item.planId.asString()
                it[startDate] = item.startDate.takeIf { date -> date != MIN_LOCAL_DATE }
                it[endDate] = item.endDate.takeIf { date -> date != MIN_LOCAL_DATE }
                it[isActive] = item.isActive
                it[lock] = item.lock.asString()
                it[paymentStatus] = item.paymentStatus
            }
            DbSubscriptionResponse(SubscriptionsTable.fromTransport(res), true)
        }, {
            DbSubscriptionResponse(
                data = null,
                success = false,
                errors = listOf(SbscrError(message = message ?: localizedMessage))
            )
        })
    }

    override suspend fun createSubscription(rq: DbSubscriptionRequest): DbSubscriptionResponse {
        val subscription = rq.subscription.copy(lock = SubscriptionLock(randomUuid()))
        return save(subscription)
    }

    override suspend fun readSubscription(rq: DbSubscriptionIdRequest): DbSubscriptionResponse {
        return safeTransaction(db, {
            val result = SubscriptionsTable.select { SubscriptionsTable.id.eq(rq.id.asString()) }.single()
            DbSubscriptionResponse(SubscriptionsTable.fromTransport(result), true)
        }, {
            val error = when (this) {
                is NoSuchElementException -> resultErrorNotFound(rq.id.asString())
                is IllegalArgumentException -> errorDuplication(rq.id.asString())
                else -> SbscrError(message = localizedMessage)
            }
            DbSubscriptionResponse(
                data = null,
                success = false,
                errors =
                if (error is SbscrError)
                    listOf(error)
                else
                    (error as DbSubscriptionResponse).errors
            )
        })
    }

    override suspend fun updateSubscription(rq: DbSubscriptionRequest): DbSubscriptionResponse {
        val key = rq.subscription.id.takeIf { it != SubscriptionId.NONE }?.asString() ?: return ISubscriptionRepository.resultErrorEmptyId
        val rqLock = rq.subscription.lock.takeIf { it != SubscriptionLock.NONE }?.asString()
        val newSub = rq.subscription.copy(lock = SubscriptionLock(randomUuid()))

        return safeTransaction(db, {
            val local = SubscriptionsTable.select{SubscriptionsTable.id.eq(key)}.singleOrNull()?.let {
                SubscriptionsTable.fromTransport(it)
            } ?: return@safeTransaction resultErrorNotFound(key)

            return@safeTransaction when (rqLock) {
                null, local.lock.asString() -> updateDb(newSub)
                else -> resultErrorConcurrent(rqLock, local)
            }
        }, {
            resultErrorNotFound(rq.subscription.id.asString())
        })
    }

    private fun updateDb(newSub: Subscription): DbSubscriptionResponse {
        SubscriptionsTable.update ({
            SubscriptionsTable.id.eq(newSub.id.asString())
        }) {
            it[ownerId] = newSub.ownerId.asString()
            it[planId] = newSub.planId.asString()
            it[startDate] = newSub.startDate.takeIf { date -> date != MIN_LOCAL_DATE }
            it[endDate] = newSub.endDate.takeIf { date -> date != MIN_LOCAL_DATE }
            it[isActive] = newSub.isActive
            it[lock] = newSub.lock.asString()
            it[paymentStatus] = newSub.paymentStatus
        }

        val result = SubscriptionsTable.select { SubscriptionsTable.id.eq(newSub.id.asString()) }.single()

        return DbSubscriptionResponse(data = SubscriptionsTable.fromTransport(result), success = true)
    }

    override suspend fun deleteSubscription(rq: DbSubscriptionIdRequest): DbSubscriptionResponse {
        val key = rq.id.takeIf{ it != SubscriptionId.NONE }?.asString() ?: return ISubscriptionRepository.resultErrorEmptyId
        val rqLock = rq.lock.takeIf { it != SubscriptionLock.NONE }?.asString() ?: return ISubscriptionRepository.resultErrorEmptyLock

        return safeTransaction(db, {
            val local = SubscriptionsTable.select{SubscriptionsTable.id.eq(key)}.singleOrNull()?.let {
                SubscriptionsTable.fromTransport(it)
            } ?: return@safeTransaction resultErrorNotFound(key)
            if (local.lock.asString() == rqLock) {
                SubscriptionsTable.deleteWhere { SubscriptionsTable.id eq rq.id.asString() }
                DbSubscriptionResponse(data = local, success = true)
            } else {
                resultErrorConcurrent(rqLock, local)
            }
        }, {
            resultErrorNotFound(key)
        })
    }

    override suspend fun searchSubscription(rq: DbSubscriptionFilterRequest): DbSubscriptionsResponse {
        val filter = rq.filter
        return safeTransaction(db, {
            val results = (SubscriptionsTable).select {
                (if (filter.ownerId == SbscrUserId.NONE) Op.TRUE else SubscriptionsTable.ownerId eq filter.ownerId.asString()) and
                (if (filter.planId == PlanId.NONE) Op.TRUE else SubscriptionsTable.planId eq filter.planId.asString()) and
                (if (filter.boughtPeriod == null) Op.TRUE else ((SubscriptionsTable.startDate greaterEq filter.boughtPeriod!!.startDate) and (SubscriptionsTable.startDate lessEq filter!!.boughtPeriod!!.endDate)) ) and
                (if (filter.expirationPeriod == null) Op.TRUE else ((SubscriptionsTable.endDate greaterEq filter.expirationPeriod!!.startDate) and (SubscriptionsTable.endDate lessEq filter!!.expirationPeriod!!.endDate)) ) and
                (if (filter.isActive == null) Op.TRUE else (SubscriptionsTable.isActive eq filter.isActive!! ))
            }
            DbSubscriptionsResponse(data = results.map { SubscriptionsTable.fromTransport(it) }, success = true)
            }, {
            DbSubscriptionsResponse(data = emptyList(), success = false, listOf(SbscrError(message = localizedMessage)))
        }
        )
    }
}
package repos

import SqlConnector
import com.benasher44.uuid.uuid4
import models.SbscrError
import models.plan.Plan
import models.plan.PlanId
import models.plan.PlanLock
import org.jetbrains.exposed.sql.*
import repo.plan.*
import repo.plan.IPlanRepository.Companion.errorDuplication
import repo.plan.IPlanRepository.Companion.resultErrorConcurrent
import repo.plan.IPlanRepository.Companion.resultErrorEmptyId
import repo.plan.IPlanRepository.Companion.resultErrorEmptyLock
import repo.plan.IPlanRepository.Companion.resultErrorNotFound
import tables.PlansTable
import java.lang.IllegalArgumentException
import java.util.NoSuchElementException

class RepoPlanSql(
    private val db: Database,
    initObjects: Collection<Plan> = emptyList(),
    val randomUuid: () -> String = { uuid4().toString() },
) : IPlanRepository {

    constructor(
        url: String = "jdbc:postgresql://localhost:5432/sbscrdevdb",
        user: String = "postgres",
        password: String = "sbscr-pass",
        schema: String = "sbscr",
        initObjects: Collection<Plan> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() }
    ) : this(SqlConnector(url, user, password, schema).connect(PlansTable), initObjects, randomUuid)

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(item: Plan): DbPlanResponse {
        return safeTransaction(db, {
            val res = PlansTable.insert {
                if (item.id != PlanId.NONE) {
                    it[id] = item.id.asString()
                } else {
                    it[id] = randomUuid()
                }
                it[title] = item.title
                it[conditions] = item.conditions.joinToString("~~")
                it[duration] = item.duration
                it[price] = item.price
                it[lock] = item.lock.asString()
                it[visibility] = item.visibility
            }
            DbPlanResponse(PlansTable.fromTransport(res), true)
        }, {
            DbPlanResponse(
                data = null,
                success = false,
                errors = listOf(SbscrError(message = message ?: localizedMessage))
            )
        })
    }
    override suspend fun createPlan(rq: DbPlanRequest): DbPlanResponse {
        val plan = rq.plan.copy(lock = PlanLock(randomUuid()))
        return save(plan)
    }

    override suspend fun readPlan(rq: DbPlanIdRequest): DbPlanResponse {
        return safeTransaction(db, {
            val result = PlansTable.select{PlansTable.id.eq(rq.id.asString())}.single()
            DbPlanResponse(PlansTable.fromTransport(result), true)
        }, {
            val error = when (this) {
                is NoSuchElementException -> resultErrorNotFound(rq.id.asString())
                is IllegalArgumentException -> errorDuplication(rq.id.asString())
                else -> SbscrError(message = localizedMessage)
            }
            DbPlanResponse(
                data = null,
                success = false,
                errors =
                    if (error is SbscrError)
                        listOf(error)
                    else
                        (error as DbPlanResponse).errors
            )
        })
    }

    override suspend fun updatePlan(rq: DbPlanRequest): DbPlanResponse {
        val key = rq.plan.id.takeIf { it != PlanId.NONE }?.asString() ?: return resultErrorEmptyId
        val rqLock = rq.plan.lock.takeIf { it != PlanLock.NONE }?.asString()
        val newPlan = rq.plan.copy(lock = PlanLock(randomUuid()))

        return safeTransaction(db, {
            val local = PlansTable.select{PlansTable.id.eq(key)}.singleOrNull()?.let {
                PlansTable.fromTransport(it)
            } ?: return@safeTransaction resultErrorNotFound(key)

            return@safeTransaction when (rqLock) {
                null, local.lock.asString() -> updateDb(newPlan)
                else -> resultErrorConcurrent(rqLock, local)
            }
        }, {
            resultErrorNotFound(rq.plan.id.asString())
        })
    }

    private fun updateDb(newPlan: Plan): DbPlanResponse {
        PlansTable.update ({
            PlansTable.id.eq(newPlan.id.asString())
        }) {
            it[title] = newPlan.title
            it[conditions] = newPlan.conditions.joinToString("~~")
            it[duration] = newPlan.duration
            it[price] = newPlan.price
            it[lock] = newPlan.lock.asString()
            it[visibility] = newPlan.visibility
        }

        val result = PlansTable.select { PlansTable.id.eq(newPlan.id.asString()) }.single()

        return DbPlanResponse(data = PlansTable.fromTransport(result), success = true)
    }

    override suspend fun deletePlan(rq: DbPlanIdRequest): DbPlanResponse {
        val key = rq.id.takeIf{ it != PlanId.NONE }?.asString() ?: return resultErrorEmptyId
        val rqLock = rq.lock.takeIf { it != PlanLock.NONE }?.asString() ?: return resultErrorEmptyLock

        return safeTransaction(db, {
            val local = PlansTable.select{PlansTable.id.eq(key)}.singleOrNull()?.let {
                PlansTable.fromTransport(it)
            } ?: return@safeTransaction resultErrorNotFound(key)
            if (local.lock.asString() == rqLock) {
                PlansTable.deleteWhere { PlansTable.id eq rq.id.asString() }
                DbPlanResponse(data = local, success = true)
            } else {
                resultErrorConcurrent(rqLock, local)
            }
        }, {
            resultErrorNotFound(key)
        })
    }

    override suspend fun readAllPlans(): DbPlansResponse {
        return safeTransaction(db, {
            val result = PlansTable.selectAll()
            DbPlansResponse(result.map { PlansTable.fromTransport(it) }, true)
        }, {
            val error =  SbscrError(message = localizedMessage)

            DbPlansResponse(
                data = null,
                success = false,
                errors = listOf(error)
            )
        })
    }
}
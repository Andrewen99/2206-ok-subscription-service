package plan

import com.benasher44.uuid.uuid4
import helpers.errorRepoConcurrency
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import models.SbscrError
import models.plan.Plan
import models.plan.PlanId
import models.plan.PlanLock
import repo.plan.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class PlanRepoInMemory(
    initObjects: List<Plan> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() }
) : IPlanRepository {
    /**
     * Инициализация кеша с установкой "времени жизни" данных после записи
     */
    private val cache = Cache.Builder()
        .expireAfterWrite(ttl)
        .build<String, PlanEntity>()
    private val mutex: Mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(plan: Plan) {
        val entity = PlanEntity(plan)
        if (entity.id == null)
            return
        cache.put(entity.id, entity)
    }

    override suspend fun createPlan(rq: DbPlanRequest): DbPlanResponse {
        val key = randomUuid()
        val plan = rq.plan.copy(id = PlanId(key), lock = PlanLock(randomUuid()))
        val entity = PlanEntity(rq.plan)
        cache.put(key, entity)
        return DbPlanResponse(
            data = plan,
            success = true
        )
    }

    override suspend fun readPlan(rq: DbPlanIdRequest): DbPlanResponse {
        val key = rq.id.takeIf { it != PlanId.NONE }?.asString() ?: return resultErrorEmptyId
        return cache.get(key)
            ?.let {
                DbPlanResponse(
                    data = it.toInternal(),
                    success = true
                )
            } ?: resultErrorNotFound
    }

    override suspend fun readAllPlans(): DbPlansResponse {
        return DbPlansResponse (
            data = cache.asMap().values.toList().map { it.toInternal() },
            success = true)
    }

    override suspend fun updatePlan(rq: DbPlanRequest): DbPlanResponse {
        val key = rq.plan.id.takeIf { it != PlanId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.plan.lock.takeIf { it != PlanLock.NONE }?.asString() ?: return resultErrorEmptyLock
        val newPlan = rq.plan.copy(lock = PlanLock((randomUuid())))
        val entity = PlanEntity(newPlan)
        return mutex.withLock {
            val oldPlan = cache.get(key)
            when {
                oldPlan == null -> resultErrorNotFound
                oldPlan.lock != oldLock -> DbPlanResponse(
                    data = oldPlan.toInternal(),
                    success = false,
                    errors = listOf(errorRepoConcurrency(oldLock, oldPlan.lock))
                )

                else -> {
                    cache.put(key, entity)
                    DbPlanResponse (
                        data = newPlan,
                        success = true
                    )
                }
            }
        }
    }

    override suspend fun deletePlan(rq: DbPlanIdRequest): DbPlanResponse {
        val key = rq.id.takeIf{ it != PlanId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.lock.takeIf { it != PlanLock.NONE }?.asString() ?: return resultErrorEmptyLock
        return mutex.withLock {
            val oldPlan = cache.get(key)
            when {
                oldPlan == null -> resultErrorNotFound
                oldPlan.lock != oldLock -> DbPlanResponse(
                    data = oldPlan.toInternal(),
                    success = false,
                    errors = listOf(errorRepoConcurrency(oldLock, oldPlan.lock))
                )

                else -> {
                    cache.invalidate(key)
                    DbPlanResponse(
                        data = oldPlan.toInternal(),
                        success = true
                    )
                }
            }
        }
    }

    companion object {
        val resultErrorEmptyId = DbPlanResponse(
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

        val resultErrorEmptyLock = DbPlanResponse(
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

        val resultErrorNotFound = DbPlanResponse(
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
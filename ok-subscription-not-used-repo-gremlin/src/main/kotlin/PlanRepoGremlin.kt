import CommonGremlinConst.FIELD_LOCK
import CommonGremlinConst.FIELD_TMP_RESULT
import CommonGremlinConst.RESULT_LOCK_FAILURE
import com.benasher44.uuid.uuid4
import exceptions.DbDuplicatedElementsException
import helpers.asSbscrError
import helpers.errorAdministration
import helpers.errorRepoConcurrency
import mappers.addPlan
import mappers.label
import mappers.listPlan
import mappers.toPlan
import models.plan.Plan
import models.plan.PlanId
import models.plan.PlanLock
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as gr
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.exception.ResponseException
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.structure.Vertex
import repo.plan.*
import repo.plan.IPlanRepository.Companion.resultErrorEmptyId
import repo.plan.IPlanRepository.Companion.resultErrorEmptyLock
import repo.plan.IPlanRepository.Companion.resultErrorNotFound
import java.lang.RuntimeException

class PlanRepoGremlin(
    private val hosts: String,
    private val port: Int = 8182,
    private val enableSsl: Boolean = false,
    initObjects: List<Plan> = emptyList(),
    initRepo: ((GraphTraversalSource) -> Unit)? = null,
    val randomUuid: () -> String = { uuid4().toString() }
) : IPlanRepository  {

    val initializedObjects: List<Plan>

    private val cluster by lazy {
        Cluster.build().apply {
            addContactPoints(*hosts.split(Regex("\\s*,\\s*")).toTypedArray())
            port(port)
            enableSsl(enableSsl)
        }.create()
    }

    private val g by lazy { traversal().withRemote(DriverRemoteConnection.using(cluster))}

    init {
        if (initRepo != null) {
            initRepo(g)
        }
        initializedObjects = initObjects.map { save(it) }
    }

    private fun save(plan: Plan) : Plan = g.addV(plan.label())
        .addPlan(plan)
        .listPlan()
        .next()
        ?.toPlan()
        ?: throw RuntimeException("Cannot initialize object $plan")

    override suspend fun createPlan(rq: DbPlanRequest): DbPlanResponse {
        val key = randomUuid()
        val plan = rq.plan.copy(id = PlanId(key), lock = PlanLock(randomUuid()))
        val dbRes = try {
            g.addV(plan.label())
                .addPlan(plan)
                .listPlan()
                .toList()
        } catch (e: Throwable) {
            if (e is ResponseException  || e.cause is ResponseException) {
                return IPlanRepository.resultErrorNotFound(key)
            }
            return DbPlanResponse(
                data = null,
                success = false,
                errors = listOf(e.asSbscrError())
            )
        }
        return when (dbRes.size) {
            0 -> IPlanRepository.resultErrorNotFound(key)
            1 -> DbPlanResponse(
                data = dbRes.first().toPlan(),
                success = true
            )
            else -> errorDuplication(key)
        }

    }

    override suspend fun readPlan(rq: DbPlanIdRequest): DbPlanResponse {
        val key = rq.id.takeIf { it != PlanId.NONE }?.asString() ?: return resultErrorEmptyId
        val dbRes = try {
            g.V(key).hasLabel(Plan::class.simpleName).listPlan().toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultErrorNotFound(key)
            }
            return DbPlanResponse(
                data = null,
                success = false,
                errors = listOf(e.asSbscrError())
            )
        }
        return when (dbRes.size) {
            0 -> resultErrorNotFound(key)
            1 -> DbPlanResponse(
                data = dbRes.first().toPlan(),
                success = true
            )
            else -> errorDuplication(key)
        }
    }

    override suspend fun updatePlan(rq: DbPlanRequest): DbPlanResponse {
        val key = rq.plan.id.takeIf { it != PlanId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.plan.lock.takeIf { it != PlanLock.NONE } ?: return resultErrorEmptyLock
        val newLock = PlanLock(randomUuid())
        val newPlan = rq.plan.copy(lock = newLock)
        val dbRes = try {
            g
                .V(key)
                .hasLabel(Plan::class.simpleName)
                .`as`("p")
                .choose( //
                    gr.select<Vertex, Any>("p")
                        .values<String>(FIELD_LOCK)
                        .`is`(oldLock.asString()),
                    gr.select<Vertex, Vertex>("p").addPlan(newPlan).listPlan(), // выполняется если лок из бд совпадает с локом из запроса
                    gr.select<Vertex, Vertex>("p").listPlan(result = RESULT_LOCK_FAILURE) // выполняется если лок из бд не совпадает с локом из запроса
                )
                .toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultErrorNotFound(key)
            }
            return DbPlanResponse(
                data = null,
                success = true,
                errors = listOf(e.asSbscrError())
            )
        }
        val planResult = dbRes.firstOrNull()?.toPlan()
        return when {
            planResult == null -> resultErrorNotFound(key)
            dbRes.size > 1 -> errorDuplication(key)
            planResult.lock != newLock -> DbPlanResponse(
                data = planResult,
                success = false,
                errors = listOf(
                    errorRepoConcurrency(
                        expectedLock = oldLock.asString(),
                        actualLock = planResult.lock.asString()
                    )
                )
            )
            else -> DbPlanResponse(
                data = planResult,
                success = true
            )
        }
    }

    override suspend fun deletePlan(rq: DbPlanIdRequest): DbPlanResponse {
        val key = rq.id.takeIf { it != PlanId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.lock.takeIf { it != PlanLock.NONE } ?: return resultErrorEmptyLock
        val dbRes = try {
            g
                .V(key)
                .hasLabel(Plan::class.simpleName)
                .`as`("p")
                .choose(
                    gr.select<Vertex, Any>("p")
                        .values<String>(FIELD_LOCK)
                        .`is`(oldLock.asString()),
                    gr.select<Vertex, Vertex>("a")
                        .sideEffect(gr.drop<Vertex>())
                        .listPlan(),
                    gr.select<Vertex, Vertex>("p")
                        .listPlan(result = RESULT_LOCK_FAILURE)
                )
                .toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultErrorNotFound(key)
            }
            return DbPlanResponse(
                data = null,
                success = true,
                errors = listOf(e.asSbscrError())
            )
        }
        val dbFirst = dbRes.firstOrNull()
        val planResult = dbFirst?.toPlan()
        return when {
            planResult == null -> resultErrorNotFound(key)
            dbRes.size > 1 -> errorDuplication(key)
            dbFirst[FIELD_TMP_RESULT] == RESULT_LOCK_FAILURE -> DbPlanResponse(
                data = planResult,
                success = false,
                errors = listOf(
                    errorRepoConcurrency(
                        expectedLock = oldLock.asString(),
                        actualLock = planResult.lock.asString()
                    )
                )
            )
            else -> DbPlanResponse(
                data = planResult,
                success = true
            )
        }
    }

    override suspend fun readAllPlans(): DbPlansResponse {
        val dbRes = try {
            g.V().hasLabel(Plan::class.simpleName).listPlan().toList()
        } catch (e: Throwable) {
            return DbPlansResponse(
                data = null,
                success = false,
                errors = listOf(e.asSbscrError())
            )
        }
        return DbPlansResponse(
                data = dbRes.map {  it.toPlan() },
                success = true
            )

    }

    companion object {
    }
}
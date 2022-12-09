import com.benasher44.uuid.uuid4
import models.plan.Plan
import models.plan.SbscrPlanVisibility
import models.subscription.Subscription
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.testcontainers.containers.PostgreSQLContainer
import repos.RepoPlanSql
import repos.RepoSubscriptionSql
import tables.PlansTable
import tables.SubscriptionsTable
import tables.UsersTable
import java.time.Duration

class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13.2")

object SqlTestCompanion {
    private const val USER = "postgres"
    private const val PASS = "sbscr-pass"
    private const val SCHEMA = "sbscr"

    private val container by lazy {
        PostgresContainer().apply {
            withUsername(USER)
            withPassword(PASS)
            withDatabaseName(SCHEMA)
            withStartupTimeout(Duration.ofSeconds(300L))
            start()
        }
    }

    private val url: String by lazy { container.jdbcUrl }

    fun planRepoUnderTestContainer(
        initObjects: Collection<Plan> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ): RepoPlanSql {
        return RepoPlanSql(url, USER, PASS, SCHEMA, initObjects, randomUuid = randomUuid)
    }

    fun planAndSubscriptionRepoUnderTestContainer(
        initPlanObjects: Collection<Plan> = emptyList(),
        initSubObjects: Collection<Subscription> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ): Pair<RepoPlanSql, RepoSubscriptionSql> {
        val db = SqlConnector(url, USER, PASS, SCHEMA).connect(
            tables = arrayOf(UsersTable, SubscriptionsTable, PlansTable),
            defaultDbAction = SqlConnector.DefaultDbAction.DROP_AND_RECREATE
        )
        val repoPlanSql = RepoPlanSql(db, initPlanObjects, randomUuid)
        val planIdSet = initSubObjects
            .map { it.planId.asString() }
            .filter { !initPlanObjects.any { plan -> plan.id.asString() == it } } // оставляем id планов, которые не присутствуют в iniPlanObjects
            .toSet()
        transaction(db) {
            planIdSet.forEach { planId ->
                PlansTable.insertStub(planId) // заполнение БД планов, чтобы избежать ошибок с внешним ключом
            }
        }
        val repoSubscriptionSql = RepoSubscriptionSql(db, initSubObjects, randomUuid = randomUuid)
        return Pair(repoPlanSql, repoSubscriptionSql)
    }

    fun subscriptionRepoUnderTestContainer(
        initSubObjects: Collection<Subscription> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ): RepoSubscriptionSql {
        val db = SqlConnector(url, USER, PASS, SCHEMA).connect(PlansTable, SubscriptionsTable, UsersTable)
        val planIdSet = initSubObjects.map { it.planId.asString() }.toSet()
        transaction(db) {
            planIdSet.forEach { planId ->
                PlansTable.insertStub(planId) // заполнение БД планов, чтобы избежать ошибок с внешним ключом
            }
        }
        return RepoSubscriptionSql(db, initSubObjects, randomUuid = randomUuid)
    }

    private fun PlansTable.insertStub(planId: String) {
        insert {
            it[id] = planId
            it[title] = "title"
            it[conditions] = "cond1~~cond2"
            it[duration] = 0
            it[price] = "0"
            it[lock] = "lock-1"
            it[visibility] = SbscrPlanVisibility.PUBLIC
        }
    }
}
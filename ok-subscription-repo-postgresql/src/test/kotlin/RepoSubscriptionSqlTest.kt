import models.plan.PlanId
import models.plan.SbscrPlanVisibility
import models.subscription.Subscription
import models.subscription.SubscriptionId
import org.jetbrains.exposed.sql.insert
import repo.subscription.ISubscriptionRepository
import subscription.*
import tables.PlansTable


class RepoSubscriptionSqlCreateTest : RepoSubscriptionCreateTest() {
    override val repo: ISubscriptionRepository = SqlTestCompanion.subscriptionRepoUnderTestContainer(
        listOf(Subscription(id = SubscriptionId("1234"), planId = PlanId("plan-id-123"))),
        randomUuid = { lockNew.asString() },
    )
}

class RepoSubscriptionSqlUpdateTest : RepoSubscriptionUpdateTest() {
    override val repo: ISubscriptionRepository = SqlTestCompanion.subscriptionRepoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}

class RepoSubscriptionSqlReadTest : RepoSubscriptionReadTest() {
    override val repo: ISubscriptionRepository = SqlTestCompanion.subscriptionRepoUnderTestContainer(
        initObjects
    )
}

class RepoSubscriptionSqlDeleteTest : RepoSubscriptionDeleteTest() {
    override val repo: ISubscriptionRepository = SqlTestCompanion.subscriptionRepoUnderTestContainer(
        initObjects
    )
}

class RepoSubscriptionSqlSearchTest : RepoSubscriptionSearchTest() {
    override val repo: ISubscriptionRepository = SqlTestCompanion.subscriptionRepoUnderTestContainer(
        initObjects
    )
}

fun PlansTable.insertStub(planId: String) {
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



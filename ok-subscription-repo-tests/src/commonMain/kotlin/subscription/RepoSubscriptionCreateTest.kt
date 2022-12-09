package subscription

import kotlinx.coroutines.ExperimentalCoroutinesApi
import models.SbscrUserId
import models.plan.PlanId
import models.subscription.Subscription
import models.subscription.SubscriptionId
import repo.subscription.DbSubscriptionRequest
import repo.subscription.ISubscriptionRepository
import runRepoTest
import util.getNextMonthAsLocalDate
import util.getYesterdayAsLocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoSubscriptionCreateTest {
    abstract val repo: ISubscriptionRepository

    protected open val createObj = Subscription(
        planId = PlanId("plan-id-123"),
        startDate = getYesterdayAsLocalDate(),
        endDate = getNextMonthAsLocalDate(),
        ownerId = SbscrUserId("user-id-123"),
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createSubscription(DbSubscriptionRequest(createObj))
        val expected = createObj.copy(id = result.data?.id ?: SubscriptionId.NONE)
        assertTrue(result.success)
        assertEquals(expected.id, result.data?.id)
        assertEquals(expected.planId, result.data?.planId)
        assertEquals(expected.startDate, result.data?.startDate)
        assertEquals(expected.endDate, result.data?.endDate)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockNew, result.data?.lock)
    }

    companion object: BaseInitSubscriptions("create") {
        override val initObjects: List<Subscription> = emptyList()
    }
}
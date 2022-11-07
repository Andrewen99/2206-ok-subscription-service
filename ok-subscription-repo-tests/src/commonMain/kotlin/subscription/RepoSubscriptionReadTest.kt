package subscription

import models.SbscrError
import models.SbscrUserId
import models.plan.PlanId
import models.subscription.Subscription
import models.subscription.SubscriptionId
import models.subscription.SubscriptionLock
import plan.BaseInitPlans
import repo.subscription.DbSubscriptionIdRequest
import repo.subscription.DbSubscriptionRequest
import repo.subscription.ISubscriptionRepository
import runRepoTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

abstract class RepoSubscriptionReadTest {
    abstract val repo: ISubscriptionRepository
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readSubscription(DbSubscriptionIdRequest(readSucc))

        assertTrue(result.success)
        assertEquals(readSucc, result.data)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readSubscription(DbSubscriptionIdRequest(notFoundId))

        assertFalse(result.success)
        assertEquals(null, result.data)
        assertEquals(
            listOf(SbscrError(field = "id", code= "notFound")),
            result.errors
        )
    }

    companion object : BaseInitSubscriptions("read") {
        override val initObjects: List<Subscription> = listOf(
            createInitTestModel(
                suf = "read"
            )
        )

        private val notFoundId = SubscriptionId("subscription-repo-read-not-found")
    }
}
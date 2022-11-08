package subscription

import createConcurrencyAssertionErrorText
import createNotFoundAssertionErrorText
import models.SbscrError
import models.SbscrUserId
import models.plan.PlanId

import models.subscription.Subscription
import models.subscription.SubscriptionId
import models.subscription.SubscriptionLock
import plan.RepoPlanDeleteTest
import repo.subscription.DbSubscriptionIdRequest
import repo.subscription.ISubscriptionRepository
import runRepoTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

abstract class RepoSubscriptionDeleteTest {
    abstract val repo: ISubscriptionRepository
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deleteSubscription(DbSubscriptionIdRequest(deleteSucc))

        assertTrue(result.success)
        assertEquals(emptyList(), result.errors)
        assertEquals(deleteSucc.lock, result.data?.lock)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.deleteSubscription(DbSubscriptionIdRequest(notFoundId, lock = lockOld))

        assertFalse(result.success)
        assertTrue(
            result.errors.any { it.field == "id" && it.group == "repo" && it.code == "not-found" },
            createNotFoundAssertionErrorText(result.errors)
        )
    }

    @Test
    fun deleteConcurrencyError() = runRepoTest {
        val result = repo.deleteSubscription(DbSubscriptionIdRequest(deleteConc.id, lockBad))

        assertFalse(result.success)
        assertTrue(
            result.errors.any { it.field == "lock" && it.group == "repo" && it.code == "concurrency" },
            createConcurrencyAssertionErrorText(result.errors)
        )
    }

    companion object: BaseInitSubscriptions("delete") {
        override val initObjects: List<Subscription> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("delete-lock")
        )
        val notFoundId = SubscriptionId("Subscription-repo-delete-notFound")
    }
}
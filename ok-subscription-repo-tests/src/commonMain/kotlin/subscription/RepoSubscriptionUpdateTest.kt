package subscription

import createConcurrencyAssertionErrorText
import createNotFoundAssertionErrorText
import models.SbscrError
import models.SbscrUserId
import models.plan.PlanId
import models.plan.PlanLock
import models.subscription.SbscrPaymentStatus
import models.subscription.Subscription
import models.subscription.SubscriptionId
import models.subscription.SubscriptionLock
import plan.RepoPlanUpdateTest
import repo.subscription.DbSubscriptionIdRequest
import repo.subscription.DbSubscriptionRequest
import repo.subscription.ISubscriptionRepository
import runRepoTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

abstract class RepoSubscriptionUpdateTest {
    abstract val repo: ISubscriptionRepository
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]

    private val reqUpdateSucc by lazy {
        Subscription(
            id = updateSucc.id,
            ownerId = updateSucc.ownerId,
            planId = updateSucc.planId,
            startDate = updateSucc.startDate,
            endDate = updateSucc.endDate,
            isActive = true,
            lock = updateSucc.lock,
            paymentStatus = SbscrPaymentStatus.PAYED
        )
    }

    private val reqUpdateNotFound by lazy {
        Subscription(
            id = updateIdNotFound,
            lock = initObjects[0].lock
        )
    }

    private val reqUpdateConc by lazy {
        Subscription(
            id = updateConc.id,
            ownerId = updateConc.ownerId,
            planId = updateConc.planId,
            startDate = updateConc.startDate,
            endDate = updateConc.endDate,
            isActive = true,
            lock = lockBad,
            paymentStatus = SbscrPaymentStatus.PAYED
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateSubscription(DbSubscriptionRequest(reqUpdateSucc))
        assertTrue(result.success)
        assertEquals(reqUpdateSucc.id, result.data?.id)
        assertEquals(reqUpdateSucc.planId, result.data?.planId)
        assertEquals(reqUpdateSucc.ownerId, result.data?.ownerId)
        assertEquals(reqUpdateSucc.startDate, result.data?.startDate)
        assertEquals(reqUpdateSucc.endDate, result.data?.endDate)
        assertEquals(reqUpdateSucc.paymentStatus, result.data?.paymentStatus)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockNew, result.data?.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateSubscription(DbSubscriptionRequest(reqUpdateNotFound))
        assertFalse(result.success)
        assertTrue(
            result.errors.any { it.field == "id" && it.group == "repo" && it.code == "not-found" },
            createNotFoundAssertionErrorText(result.errors)
        )
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateSubscription(DbSubscriptionRequest(reqUpdateConc))
        assertFalse(result.success)
        assertTrue(
            result.errors.any { it.field == "lock" && it.group == "repo" && it.code == "concurrency" },
            createConcurrencyAssertionErrorText(result.errors)
        )
    }

    companion object: BaseInitSubscriptions("update") {
        override val initObjects: List<Subscription> = listOf(
            createInitTestModel("update-succ"),
            createInitTestModel("update-conc")
        )

        val updateIdNotFound = SubscriptionId("update-not-found")

    }
}
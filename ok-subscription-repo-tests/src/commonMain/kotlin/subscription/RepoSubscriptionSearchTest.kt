package subscription

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import models.SbscrDatePeriod
import models.SbscrUserId
import models.plan.PlanId
import models.subscription.SbscrPaymentStatus
import models.subscription.Subscription
import repo.subscription.DbSubscriptionFilterRequest
import repo.subscription.ISubscriptionRepository
import runRepoTest
import util.getLastMonthAsLocalDate
import util.getNextMonthAsLocalDate
import util.getYesterdayAsLocalDate
import util.inPeriod
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class RepoSubscriptionSearchTest {
    abstract val repo: ISubscriptionRepository

    protected open val initializedObjects: List<Subscription> = initObjects

    @Test
    fun searchStartDatePeriod() = runRepoTest {
        val lastMonthPeriod = SbscrDatePeriod(
            startDate = getLastMonthAsLocalDate().minus(5, DateTimeUnit.DAY),
            endDate = getLastMonthAsLocalDate().plus(5, DateTimeUnit.DAY)
        )
        val result = repo.searchSubscription(DbSubscriptionFilterRequest(startPeriod = lastMonthPeriod, endPeriod = null))
        assertTrue(result.success)
        assertEquals(
            initializedObjects.filter { it.startDate inPeriod lastMonthPeriod },
            result.data
        )
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchEndDatePeriod() = runRepoTest {
        val recentDaysPeriod = SbscrDatePeriod(
            startDate = getYesterdayAsLocalDate().minus(5, DateTimeUnit.DAY),
            endDate = getYesterdayAsLocalDate().plus(5, DateTimeUnit.DAY),
        )
        val result = repo.searchSubscription(DbSubscriptionFilterRequest(startPeriod = null, endPeriod = recentDaysPeriod))
        assertTrue(result.success)
        assertEquals(
            initializedObjects.filter { it.endDate inPeriod recentDaysPeriod },
            result.data
        )
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchPlanId() = runRepoTest {
        val result = repo.searchSubscription(DbSubscriptionFilterRequest(specialPlanId))
        assertTrue(result.success)
        assertEquals(
            initializedObjects.filter { it.planId == specialPlanId },
            result.data
        )
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchOwnerId() = runRepoTest {
        val result = repo.searchSubscription(DbSubscriptionFilterRequest(specialUserId))
        assertTrue(result.success)
        assertEquals(
            initializedObjects.filter { it.ownerId == specialUserId },
            result.data
        )
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchIsActive() = runRepoTest {
        val result = repo.searchSubscription(DbSubscriptionFilterRequest(isActive = true))
        assertTrue(result.success)
        assertEquals(
            initializedObjects.filter { it.isActive },
            result.data
        )
        assertEquals(emptyList(), result.errors)
    }

    companion object: BaseInitSubscriptions("search") {
        val specialPlanId = PlanId("special-plan-id-123")
        val specialUserId = SbscrUserId("special-user-id-123")
        override val initObjects: List<Subscription> = listOf(
            createInitTestModel("sub1"),
            createInitTestModel(suf = "sub2-last-month-start", startDate = getLastMonthAsLocalDate()),
            createInitTestModel(suf = "sub3-yesterday-end", startDate = getLastMonthAsLocalDate(), endDate = getYesterdayAsLocalDate()),
            createInitTestModel(suf = "sub4-special-plan-id", planId = specialPlanId),
            createInitTestModel(suf = "sub5-is-active-true", isActive = true),
            createInitTestModel(suf = "sub6-special-user-id", ownerId = specialUserId)
        )



    }
}
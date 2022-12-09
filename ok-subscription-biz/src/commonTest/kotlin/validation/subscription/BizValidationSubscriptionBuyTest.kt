package validation.subscription

import PlanProcessor
import PlanRepoStub
import SubscriptionProcessor
import SubscriptionRepoStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import models.RepoSettings
import models.plan.PlanRepoSettings
import models.subscription.SubscriptionCommand
import models.subscription.SubscriptionRepoSettings

import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationSubscriptionBuyTest {

    private val command = SubscriptionCommand.BUY
    private val settings by lazy {
        RepoSettings(
           PlanRepoSettings(repoTest = PlanRepoStub()),
            SubscriptionRepoSettings(repoTest = SubscriptionRepoStub())
        )
    }
    private val processor by lazy { SubscriptionProcessor(settings) }

    @Test
    fun correctPlanId() = validationPlanIdCorrect(command, processor)
    @Test
    fun trimPlanId() = validationPlanIdTrim(command, processor)
    @Test
    fun emptyPlanId() = validationPlanIdEmpty(command, processor)
    @Test
    fun planIdFormat() = validationPlanIdFormat(command, processor)
}
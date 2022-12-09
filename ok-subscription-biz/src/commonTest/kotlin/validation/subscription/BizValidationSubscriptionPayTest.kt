package validation.subscription

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
class BizValidationSubscriptionPayTest {
    private val command = SubscriptionCommand.PAY
    private val settings by lazy {
        RepoSettings(
            PlanRepoSettings(repoTest = PlanRepoStub()),
            SubscriptionRepoSettings(repoTest = SubscriptionRepoStub())
        )
    }
    private val processor by lazy { SubscriptionProcessor(settings) }

    @Test
    fun correctId() = validationIdCorrect(command, processor)
    @Test
    fun trimId() = validationIdTrim(command, processor)
    @Test
    fun emptyId() = validationIdEmpty(command, processor)
    @Test
    fun idFormat() = validationIdFormat(command, processor)
}
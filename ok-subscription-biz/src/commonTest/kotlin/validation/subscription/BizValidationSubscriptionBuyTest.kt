package validation.subscription

import SubscriptionProcessor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import models.subscription.SubscriptionCommand

import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationSubscriptionBuyTest {

    private val command = SubscriptionCommand.BUY
    private val processor by lazy { SubscriptionProcessor() }

    @Test
    fun correctPlanId() = validationPlanIdCorrect(command, processor)
    @Test
    fun trimPlanId() = validationPlanIdTrim(command, processor)
    @Test
    fun emptyPlanId() = validationPlanIdEmpty(command, processor)
    @Test
    fun planIdFormat() = validationPlanIdFormat(command, processor)
}
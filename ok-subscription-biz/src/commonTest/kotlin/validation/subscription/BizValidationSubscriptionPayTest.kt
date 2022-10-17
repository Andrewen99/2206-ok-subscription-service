package validation.subscription

import SubscriptionProcessor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import models.subscription.SubscriptionCommand
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationSubscriptionPayTest {
    private val command = SubscriptionCommand.PAY
    private val processor by lazy { SubscriptionProcessor() }

    @Test
    fun correctId() = validationIdCorrect(command, processor)
    @Test
    fun trimId() = validationIdTrim(command, processor)
    @Test
    fun emptyId() = validationIdEmpty(command, processor)
    @Test
    fun idFormat() = validationIdFormat(command, processor)
}
package validation.plan

import PlanProcessor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import models.plan.PlanCommand
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationPlanReadTest {

    private val command = PlanCommand.READ
    private val processor by lazy { PlanProcessor() }

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)
}
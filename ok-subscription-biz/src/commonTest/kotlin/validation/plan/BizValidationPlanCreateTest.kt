package validation.plan

import PlanProcessor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import models.plan.PlanCommand
import models.plan.PlanRepoSettings
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationPlanCreateTest {

    private val command = PlanCommand.CREATE
    private val settings by lazy {
//        PlanRepoSettings(
//            repoTest = PlanRepoTest
//        )
    }
    private val processor by lazy { PlanProcessor() }

    @Test fun correctTitle() = validationTitleCorrect(command, processor)
    @Test fun trimTitle() = validationTitleTrim(command, processor)
    @Test fun emptyTitle() = validationTitleEmpty(command, processor)
    @Test fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test fun correctConditions() = validationConditionsCorrect(command, processor)
    @Test fun trimConditions() = validationConditionsTrim(command, processor)
    @Test fun emptyConditions() = validationConditionsEmpty(command, processor)
    @Test fun badSymbolsConditions() = validationConditionsSymbols(command, processor)

    @Test fun correctDuration() = validationDurationCorrect(command, processor)
    @Test fun badZeroDuration() = validationDurationZero(command, processor)
    @Test fun badNegativeDuration() = validationDurationNegative(command, processor)

    @Test fun correctPrice() = validationPriceCorrect(command, processor)
    @Test fun trimPrice() = validationPriceTrim(command, processor)
    @Test fun badFormatPrice() = validationPriceSymbols(command, processor)
    @Test fun badNegativePrice() = validationPriceNegative(command, processor)

}
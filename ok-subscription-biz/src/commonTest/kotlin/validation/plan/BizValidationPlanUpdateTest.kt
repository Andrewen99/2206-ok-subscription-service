package validation.plan

import PlanProcessor
import PlanRepoStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import models.RepoSettings
import models.plan.PlanCommand
import models.plan.PlanRepoSettings
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationPlanUpdateTest {

    private val command = PlanCommand.UPDATE
    private val settings by lazy {
        RepoSettings(
            PlanRepoSettings(repoTest = PlanRepoStub()),
        )
    }
    private val processor by lazy { PlanProcessor(settings) }

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

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
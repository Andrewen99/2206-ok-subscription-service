package validation.plan

import PlanProcessor
import PlanRepoStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import models.RepoSettings
import models.plan.PlanCommand
import models.plan.PlanRepoSettings
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationPlanDeleteTest {

    private val command = PlanCommand.DELETE
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

    @Test fun correctLock() = validationLockCorrect(command, processor)
    @Test fun trimLock() = validationLockTrim(command, processor)
    @Test fun emptyLock() = validationLockEmpty(command, processor)
    @Test fun badFormatLock() = validationLockFormat(command, processor)
}
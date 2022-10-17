package stubs.plan

import kotlinx.coroutines.ExperimentalCoroutinesApi
import models.plan.PlanCommand
import kotlin.test.Test


@OptIn(ExperimentalCoroutinesApi::class)
class PlanStubValidationTest {

    @Test fun createBadTitle() = planStubBadTitle(PlanCommand.CREATE)
    @Test fun updateBadTitle() = planStubBadTitle(PlanCommand.UPDATE)

    @Test fun createBadCondition() = planStubBadConditions(PlanCommand.CREATE)
    @Test fun updateBadCondition() = planStubBadConditions(PlanCommand.UPDATE)

    @Test fun createDbError() = planStubDbError(PlanCommand.CREATE)
    @Test fun updateDbError() = planStubDbError(PlanCommand.UPDATE)
    @Test fun readDbError() = planStubDbError(PlanCommand.READ)
    @Test fun readAllDbError() = planStubDbError(PlanCommand.READ_ALL)
    @Test fun deleteDbError() = planStubDbError(PlanCommand.DELETE)

    @Test fun updateBadId() = planStubBadId(PlanCommand.UPDATE)
    @Test fun readBadId() = planStubBadId(PlanCommand.READ)
    @Test fun deleteBadId() = planStubBadId(PlanCommand.DELETE)
}
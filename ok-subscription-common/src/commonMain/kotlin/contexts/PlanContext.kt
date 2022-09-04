package contexts

import kotlinx.datetime.Instant
import models.*
import models.plan.Plan
import models.plan.PlanCommand
import NONE

/**
 * Контекст для подписок
 */
data class PlanContext(
    override var state: SbscrState = SbscrState.NONE,
    override var errors: MutableList<SbscrError> = mutableListOf(),
    override var workMode: SbscrWorkMode = SbscrWorkMode.PROD,
    override var stubCase: SbscrStubs = SbscrStubs.NONE,
    override var requestId: SbscrRequestId = SbscrRequestId.NONE,
    override var timeStart: Instant = Instant.NONE, //пока нигде не используется


    var command: PlanCommand = PlanCommand.NONE,
    var planRequest: Plan = Plan(),
    var planResponse: Plan = Plan(),
    val planResponses: MutableList<Plan> = mutableListOf(),
) : BaseContext
package contexts

import kotlinx.datetime.Instant
import models.*
import models.plan.Plan
import models.plan.PlanCommand
import util.NONE
import models.plan.PlanRepoSettings
import repo.plan.IPlanRepository

/**
 * Контекст для подписок
 */
data class PlanContext(
    override var state: SbscrState = SbscrState.NONE,
    override var errors: MutableList<SbscrError> = mutableListOf(),
    override var workMode: SbscrWorkMode = SbscrWorkMode.PROD,
    override var stubCase: SbscrStubs = SbscrStubs.NONE,
    override var requestId: SbscrRequestId = SbscrRequestId.NONE,
    override var timeStart: Instant = Instant.NONE,

    var planRepoSettings: PlanRepoSettings = PlanRepoSettings(),
    var planRepo: IPlanRepository = IPlanRepository.NONE,

    var command: PlanCommand = PlanCommand.NONE,
    var planRequest: Plan = Plan(),

    var planValidating: Plan = Plan(),
    var planValidated: Plan = Plan(),

    var planRepoRead: Plan = Plan(),
    var planRepoReadAll: MutableList<Plan> = mutableListOf(),
    var planRepoPrepare: Plan = Plan(),
    var planRepoDone: Plan = Plan(),
    var plansRepoDone: MutableList<Plan> = mutableListOf(),

    var planResponse: Plan = Plan(),
    var planResponses: MutableList<Plan> = mutableListOf(),
) : BaseContext
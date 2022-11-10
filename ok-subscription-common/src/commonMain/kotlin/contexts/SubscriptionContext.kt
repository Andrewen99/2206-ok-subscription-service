package contexts

import util.NONE
import kotlinx.datetime.Instant
import models.*
import models.plan.Plan
import models.subscription.SubscriptionCommand
import models.subscription.SubscriptionFilter
import models.subscription.Subscription
import models.subscription.SubscriptionRepoSettings
import repo.plan.IPlanRepository
import repo.subscription.ISubscriptionRepository

/**
 * Контекст для приобретенных/приобретаемых подписок
 */
data class SubscriptionContext(
    override var state: SbscrState = SbscrState.NONE,
    override var errors: MutableList<SbscrError> = mutableListOf(),
    override var workMode: SbscrWorkMode = SbscrWorkMode.PROD,
    override var stubCase: SbscrStubs = SbscrStubs.NONE,
    override var requestId: SbscrRequestId = SbscrRequestId.NONE,
    override var timeStart: Instant = Instant.NONE,

    var repoSettings: RepoSettings = RepoSettings(),
    var subscriptionRepo: ISubscriptionRepository = ISubscriptionRepository.NONE,
    var planRepo: IPlanRepository = IPlanRepository.NONE,

    var command: SubscriptionCommand = SubscriptionCommand.NONE,
    var subscriptionRequest: Subscription = Subscription(),
    var subscriptionFilter: SubscriptionFilter = SubscriptionFilter(),

    var subscriptionValidating: Subscription = Subscription(),
    var subscriptionValidated: Subscription = Subscription(),

    var subscriptionFilterValidating: SubscriptionFilter = SubscriptionFilter(),
    var subscriptionFilterValidated: SubscriptionFilter = SubscriptionFilter(),

    var planRepoRead: Plan = Plan(),
    var subscriptionRepoRead: Subscription = Subscription(),
    var subscriptionRepoPrepare: Subscription = Subscription(),
    var subscriptionRepoDone: Subscription = Subscription(),
    var subscriptionsRepoDone: MutableList<Subscription> = mutableListOf(),

    var subscriptionResponse: Subscription = Subscription(),
    var subscriptionResponses: MutableList<Subscription> = mutableListOf()
) : BaseContext

package contexts

import NONE
import kotlinx.datetime.Instant
import models.*
import models.subscription.SubscriptionCommand
import models.subscription.SubscriptionFilter
import models.subscription.Subscription
import models.plan.PlanId

/**
 * Контекст для приобретенных/приобретаемых подписок
 */
data class SubscriptionContext(
    override var state: SbscrState = SbscrState.NONE,
    override var errors: MutableList<SbscrError> = mutableListOf(),
    override var workMode: SbscrWorkMode = SbscrWorkMode.PROD,
    override var stubCase: SbscrStubs = SbscrStubs.NONE,
    override var requestId: SbscrRequestId = SbscrRequestId.NONE,
    override var timeStart: Instant = Instant.NONE, //пока нигде не используется


    var command: SubscriptionCommand = SubscriptionCommand.NONE,
    var subscriptionRequest: Subscription = Subscription(),
    var subscriptionFilter: SubscriptionFilter = SubscriptionFilter(),

    var subscriptionValidating: Subscription = Subscription(),
    var subscriptionValidated: Subscription = Subscription(),

    var subscriptionFilterValidating: SubscriptionFilter = SubscriptionFilter(),
    var subscriptionFilterValidated: SubscriptionFilter = SubscriptionFilter(),

    var subscriptionResponse: Subscription = Subscription(),
    val subscriptionResponses: MutableList<Subscription> = mutableListOf()
) : BaseContext

package models.subscription

import repo.subscription.ISubscriptionRepository


data class SubscriptionRepoSettings(
    val repoStub: ISubscriptionRepository = ISubscriptionRepository.NONE,
    val repoTest: ISubscriptionRepository = ISubscriptionRepository.NONE,
    val repoProd: ISubscriptionRepository = ISubscriptionRepository.NONE,
)
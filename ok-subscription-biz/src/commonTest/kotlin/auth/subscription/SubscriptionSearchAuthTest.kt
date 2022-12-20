package auth.subscription

import PlanRepoStub
import SubscriptionProcessor
import SubscriptionRepoStub
import auth.createPrincipal
import kotlinx.coroutines.test.runTest
import models.RepoSettings
import models.SbscrUserId
import models.plan.PlanRepoSettings
import models.subscription.SubscriptionCommand
import models.subscription.SubscriptionRepoSettings
import permissions.SbscrUserGroups
import kotlin.test.Test
import kotlin.test.assertEquals

class SubscriptionSearchAuthTest {
    private val userId = SbscrUserId("owner-id-1")
    private val command = SubscriptionCommand.SEARCH
    private val settings by lazy {
        RepoSettings(
            PlanRepoSettings(repoTest = PlanRepoStub()),
            SubscriptionRepoSettings(repoTest = SubscriptionRepoStub(userId))
        )
    }
    private val processor by lazy { SubscriptionProcessor(settings) }

    @Test
    fun subscriptionSearchAdmin() = runTest {
        val ctx = execSubscriptionProcessor(command, processor, createPrincipal(id = userId, group = SbscrUserGroups.ADMIN))
        assertAllowed(ctx)
        assertEquals(SbscrUserId.NONE ,ctx.subscriptionFilter.ownerId) //Имеет доступ на чтение всех подписок
    }

    @Test
    fun subscriptionSearchModerator() = runTest {
        val ctx = execSubscriptionProcessor(command, processor, createPrincipal(id = userId, group = SbscrUserGroups.MODERATOR))
        assertAllowed(ctx)
        assertEquals(SbscrUserId.NONE ,ctx.subscriptionFilter.ownerId)//Имеет доступ на чтение всех подписок
    }

    @Test
    fun subscriptionSearchUser() = runTest {
        val ctx = execSubscriptionProcessor(command, processor, createPrincipal(id = userId, group = SbscrUserGroups.USER))
        assertAllowed(ctx)
        assertEquals(userId ,ctx.subscriptionFilterValidated.ownerId)//Имеет доступ на чтение только своих подписок
    }
}
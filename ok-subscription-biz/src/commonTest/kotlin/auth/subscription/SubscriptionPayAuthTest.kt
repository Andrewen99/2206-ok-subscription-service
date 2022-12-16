package auth.subscription

import PlanRepoStub
import SubscriptionProcessor
import SubscriptionRepoStub
import auth.createPrincipal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.RepoSettings
import models.SbscrUserId
import models.plan.PlanRepoSettings
import models.subscription.SubscriptionCommand
import models.subscription.SubscriptionRepoSettings
import permissions.SbscrUserGroups
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SubscriptionPayAuthTest {
    private val userId = SbscrUserId("owner-id-1")
    private val command = SubscriptionCommand.PAY
    private val settings by lazy {
        RepoSettings(
            PlanRepoSettings(repoTest = PlanRepoStub()),
            SubscriptionRepoSettings(repoTest = SubscriptionRepoStub(userId))
        )
    }
    private val processor by lazy { SubscriptionProcessor(settings) }

    @Test
    fun subscriptionPayAdmin() = runTest {
        val ctx = execSubscriptionProcessor(command, processor, createPrincipal(id = userId, group = SbscrUserGroups.ADMIN))
        assertAllowed(ctx)
    }

    @Test
    fun subscriptionPayModerator() = runTest {
        val ctx = execSubscriptionProcessor(command, processor, createPrincipal(id = userId, group = SbscrUserGroups.MODERATOR))
        assertAllowed(ctx)
    }

    @Test
    fun subscriptionPayUser() = runTest {
        val ctx = execSubscriptionProcessor(command, processor, createPrincipal(id = userId, group = SbscrUserGroups.USER))
        assertAllowed(ctx)
    }

    @Test
    fun subscriptionPayUserBanned() = runTest {
        val ctx = execSubscriptionProcessor(
            command,
            processor,
            createPrincipal(id = userId, groups = setOf(SbscrUserGroups.ADMIN, SbscrUserGroups.BAN)))
        assertNotAllowed(ctx)
    }

    @Test
    fun subscriptionPayNotOnwAdmin() = runTest {
        val ctx = execSubscriptionProcessor(command, processor, createPrincipal(group = SbscrUserGroups.ADMIN))
        assertNotAllowed(ctx)
    }


    @Test
    fun subscriptionPayNotOnwModerator() = runTest {
        val ctx = execSubscriptionProcessor(command, processor, createPrincipal(group = SbscrUserGroups.MODERATOR))
        assertNotAllowed(ctx)
    }

    @Test
    fun subscriptionPayNotOnwUser() = runTest {
        val ctx = execSubscriptionProcessor(command, processor, createPrincipal(group = SbscrUserGroups.USER))
        assertNotAllowed(ctx)
    }
}
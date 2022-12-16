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
class SubscriptionReadAuthTest {
    private val userId = SbscrUserId("owner-id-1")
    private val command = SubscriptionCommand.READ
    private val settings by lazy {
        RepoSettings(
            PlanRepoSettings(repoTest = PlanRepoStub()),
            SubscriptionRepoSettings(repoTest = SubscriptionRepoStub(userId))
        )
    }
    private val processor by lazy { SubscriptionProcessor(settings) }

    @Test
    fun subscriptionReadAdmin() = runTest {
        val ctx = execSubscriptionProcessor(command, processor, createPrincipal(id = userId, group = SbscrUserGroups.ADMIN))
        assertAllowed(ctx)
    }

    @Test
    fun subscriptionReadModerator() = runTest {
        val ctx = execSubscriptionProcessor(command, processor, createPrincipal(id = userId, group = SbscrUserGroups.MODERATOR))
        assertAllowed(ctx)
    }

    @Test
    fun subscriptionReadUser() = runTest {
        val ctx = execSubscriptionProcessor(command, processor, createPrincipal(id = userId, group = SbscrUserGroups.USER))
        assertAllowed(ctx)
    }

    @Test
    fun subscriptionReadNotOnwAdmin() = runTest {
        val ctx = execSubscriptionProcessor(command, processor, createPrincipal(group = SbscrUserGroups.ADMIN))
        assertAllowed(ctx)
    }


    @Test
    fun subscriptionReadNotOnwModerator() = runTest {
        val ctx = execSubscriptionProcessor(command, processor, createPrincipal(group = SbscrUserGroups.MODERATOR))
        assertAllowed(ctx)
    }

    @Test
    fun subscriptionReadNotOnwUser() = runTest {
        val ctx = execSubscriptionProcessor(command, processor, createPrincipal(group = SbscrUserGroups.USER))
        assertNotAllowed(ctx)
    }
}
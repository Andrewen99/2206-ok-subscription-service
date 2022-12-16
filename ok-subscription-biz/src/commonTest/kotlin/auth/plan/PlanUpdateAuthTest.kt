package auth.plan

import PlanProcessor
import PlanRepoStub
import auth.createPrincipal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.RepoSettings
import models.SbscrState
import models.plan.PlanCommand
import models.plan.PlanRepoSettings
import permissions.SbscrUserGroups
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class PlanUpdateAuthTest {

    private val command = PlanCommand.UPDATE
    private val settings by lazy {
        RepoSettings(
            PlanRepoSettings(repoTest = PlanRepoStub()),
        )
    }
    private val processor by lazy { PlanProcessor(settings) }

    @Test
    fun planUpdateAdmin() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(group = SbscrUserGroups.ADMIN))
        assertAllowed(ctx)
    }
    @Test
    fun planUpdateBannedAdmin() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(groups = setOf(SbscrUserGroups.ADMIN, SbscrUserGroups.BAN)))
        assertNotAllowed(ctx)
    }

    @Test
    fun planUpdateAdminAndUserGroups() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(groups = setOf(SbscrUserGroups.ADMIN, SbscrUserGroups.USER)))
        assertAllowed(ctx)
    }

    @Test
    fun planUpdateModerator() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(group = SbscrUserGroups.MODERATOR))
        assertAllowed(ctx)
    }

    @Test
    fun planUpdateBannedModerator() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(groups = setOf(SbscrUserGroups.MODERATOR, SbscrUserGroups.BAN)))
        assertNotAllowed(ctx)
    }

    @Test
    fun planUpdateUser() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(group = SbscrUserGroups.USER))
        assertNotAllowed(ctx)
    }
}
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
class PlanDeleteAuthTest {

    private val command = PlanCommand.DELETE
    private val settings by lazy {
        RepoSettings(
            PlanRepoSettings(repoTest = PlanRepoStub()),
        )
    }
    private val processor by lazy { PlanProcessor(settings) }

    @Test
    fun planDeleteAdmin() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(group = SbscrUserGroups.ADMIN))
        assertAllowed(ctx)
    }
    @Test
    fun planDeleteBannedAdmin() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(groups = setOf(SbscrUserGroups.ADMIN, SbscrUserGroups.BAN)))
        assertNotAllowed(ctx)
    }

    @Test
    fun planDeleteAdminAndUserGroups() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(groups = setOf(SbscrUserGroups.ADMIN, SbscrUserGroups.USER)))
        assertAllowed(ctx)
    }

    @Test
    fun planDeleteModerator() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(group = SbscrUserGroups.MODERATOR))
        assertNotAllowed(ctx)
    }

    @Test
    fun planDeleteUser() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(group = SbscrUserGroups.USER))
        assertNotAllowed(ctx)
    }
}
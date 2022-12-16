package auth.plan

import PlanProcessor
import PlanRepoStub
import auth.createPrincipal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.RepoSettings
import models.plan.PlanCommand
import models.plan.PlanRepoSettings
import permissions.SbscrUserGroups
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlanCreateAuthTest {

    private val command = PlanCommand.CREATE
    private val settings by lazy {
        RepoSettings(
            PlanRepoSettings(repoTest = PlanRepoStub()),
        )
    }
    private val processor by lazy { PlanProcessor(settings) }

    @Test
    fun planCreateAdmin() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(group = SbscrUserGroups.ADMIN))
        assertAllowed(ctx)
    }
    @Test
    fun planCreateBannedAdmin() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(groups = setOf(SbscrUserGroups.ADMIN, SbscrUserGroups.BAN)))
        assertNotAllowed(ctx)
    }

    @Test
    fun planCreateAdminAndUserGroups() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(groups = setOf(SbscrUserGroups.ADMIN, SbscrUserGroups.USER)))
        assertAllowed(ctx)
    }

    @Test
    fun planCreateModerator() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(group = SbscrUserGroups.MODERATOR))
        assertNotAllowed(ctx)
    }

    @Test
    fun planCreateUser() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(group = SbscrUserGroups.USER))
        assertNotAllowed(ctx)
    }

}
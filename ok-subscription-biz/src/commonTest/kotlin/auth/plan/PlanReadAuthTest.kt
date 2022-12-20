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
import models.plan.SbscrPlanVisibility
import permissions.SbscrUserGroups
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class PlanReadAuthTest {

    private val command = PlanCommand.READ
    private val settings by lazy {
        RepoSettings(
            PlanRepoSettings(repoTest = PlanRepoStub()),
        )
    }
    private val processor by lazy { PlanProcessor(settings) }
    private val adminOnlyProcessor by lazy {PlanProcessor(
        RepoSettings(
            PlanRepoSettings(repoTest = PlanRepoStub(SbscrPlanVisibility.ADMIN_ONLY)),
        )
    )}

    @Test
    fun planReadPublicAdmin() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(group = SbscrUserGroups.ADMIN))
        assertAllowed(ctx)
    }

    @Test
    fun planReadPublicModerator() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(group = SbscrUserGroups.MODERATOR))
        assertAllowed(ctx)
    }


    @Test
    fun planReadPublicUser() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(group = SbscrUserGroups.USER))
        assertAllowed(ctx)
    }

    @Test
    fun planReadAdminOnlyAdmin() = runTest {
        val ctx = execPlanProcessor(command, adminOnlyProcessor, createPrincipal(group = SbscrUserGroups.ADMIN))
        assertAllowed(ctx)
    }

    @Test
    fun planReadAdminOnlyBannedAdmin() = runTest {
        val ctx = execPlanProcessor(command, adminOnlyProcessor, createPrincipal(groups = setOf(SbscrUserGroups.ADMIN, SbscrUserGroups.BAN)))
        assertNotAllowed(ctx)
    }

    @Test
    fun planReadAdminOnlyModerator() = runTest {
        val ctx = execPlanProcessor(command, adminOnlyProcessor, createPrincipal(group = SbscrUserGroups.MODERATOR))
        assertNotAllowed(ctx)
    }


    @Test
    fun planReadAdminOnlyUser() = runTest {
        val ctx = execPlanProcessor(command, adminOnlyProcessor, createPrincipal(group = SbscrUserGroups.USER))
        assertNotAllowed(ctx)
    }
}
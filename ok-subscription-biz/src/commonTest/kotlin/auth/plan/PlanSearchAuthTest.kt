package auth.plan

import PlanProcessor
import PlanRepoStub
import auth.createPrincipal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.RepoSettings
import models.plan.PlanCommand
import models.plan.PlanRepoSettings
import models.plan.SbscrPlanVisibility
import permissions.SbscrUserGroups
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class PlanSearchAuthTest {
    private val command = PlanCommand.READ_ALL
    private val settings by lazy {
        RepoSettings(
            PlanRepoSettings(repoTest = PlanRepoStub()),
        )
    }
    private val processor by lazy { PlanProcessor(settings) }

    @Test
    fun planReadPublicAdmin() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(group = SbscrUserGroups.ADMIN))
        assertAllowed(ctx)
        assertEquals(setOf(SbscrPlanVisibility.ADMIN_ONLY, SbscrPlanVisibility.PUBLIC),ctx.planFilterValidated.visibilitySet)
    }

    @Test
    fun planReadPublicModerator() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(group = SbscrUserGroups.MODERATOR))
        assertAllowed(ctx)
        assertEquals(setOf(SbscrPlanVisibility.PUBLIC),ctx.planFilterValidated.visibilitySet)
    }


    @Test
    fun planReadPublicUser() = runTest {
        val ctx = execPlanProcessor(command, processor, createPrincipal(group = SbscrUserGroups.USER))
        assertAllowed(ctx)
        assertEquals(setOf(SbscrPlanVisibility.PUBLIC),ctx.planFilterValidated.visibilitySet)
    }
}
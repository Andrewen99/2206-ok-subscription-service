import models.SbscrPrincipalModel
import models.plan.Plan
import models.plan.PlanId
import models.plan.SbscrPlanVisibility
import models.subscription.Subscription
import models.subscription.SubscriptionId
import permissions.SbscrPrincipalRelations
import permissions.SbscrUserGroups

fun Plan.resolveRelationsTo(principal: SbscrPrincipalModel) : Set<SbscrPrincipalRelations> = setOfNotNull(
    SbscrPrincipalRelations.NONE,
    SbscrPrincipalRelations.NEW.takeIf { id == PlanId.NONE },
    SbscrPrincipalRelations.PUBLIC.takeIf { visibility == SbscrPlanVisibility.PUBLIC },
    SbscrPrincipalRelations.FOR_ADMINS.takeIf { visibility == SbscrPlanVisibility.ADMIN_ONLY },
    SbscrPrincipalRelations.MODERATABLE.takeIf { principal.groups.any { it == SbscrUserGroups.MODERATOR || it == SbscrUserGroups.ADMIN } },
)

fun Subscription.resolveRelationsTo(principal: SbscrPrincipalModel) : Set<SbscrPrincipalRelations> = setOfNotNull(
    SbscrPrincipalRelations.NONE,
    SbscrPrincipalRelations.NEW.takeIf { id == SubscriptionId.NONE },
    SbscrPrincipalRelations.OWN.takeIf { ownerId == principal.id },
    SbscrPrincipalRelations.MODERATABLE.takeIf { principal.groups.any { it == SbscrUserGroups.MODERATOR || it == SbscrUserGroups.ADMIN } }
)
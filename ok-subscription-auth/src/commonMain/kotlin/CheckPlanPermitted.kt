import models.plan.PlanCommand
import permissions.SbscrPrincipalRelations
import permissions.UserPlanPermissions

fun checkPlanPermitted(
    command: PlanCommand,
    relations: Iterable<SbscrPrincipalRelations>,
    permissions: Iterable<UserPlanPermissions>
) =
    relations.asSequence().flatMap { relation ->
        permissions.map { permission ->
            PlanTableConditions(
                command = command,
                permission = permission,
                relation = relation
            )
        }
    }.any {
        accessTable[it] != null
    }

private data class PlanTableConditions(
    val command: PlanCommand,
    val permission: UserPlanPermissions,
    val relation: SbscrPrincipalRelations
)

private val accessTable = mapOf(
    //create (only admin)
    PlanTableConditions(
        command = PlanCommand.CREATE,
        permission = UserPlanPermissions.CREATE,
        relation = SbscrPrincipalRelations.MODERATABLE
    ) to true,

    //read
    //public
    PlanTableConditions(
        command = PlanCommand.READ,
        permission = UserPlanPermissions.READ_PUBLIC,
        relation = SbscrPrincipalRelations.NONE
    ) to true,

    PlanTableConditions(
        command = PlanCommand.READ,
        permission = UserPlanPermissions.READ_PUBLIC,
        relation = SbscrPrincipalRelations.PUBLIC
    ) to true,

    //admin only plans
    PlanTableConditions(
        command = PlanCommand.READ,
        permission = UserPlanPermissions.READ_ADMIN_ONLY,
        relation = SbscrPrincipalRelations.FOR_ADMINS
    ) to true,

    //update
    PlanTableConditions(
        command = PlanCommand.UPDATE,
        permission = UserPlanPermissions.UPDATE_CANDIDATE,
        relation = SbscrPrincipalRelations.MODERATABLE
    ) to true,

    //delete
    PlanTableConditions(
        command = PlanCommand.DELETE,
        permission = UserPlanPermissions.DELETE_CANDIDATE,
        relation = SbscrPrincipalRelations.MODERATABLE
    ) to true
)
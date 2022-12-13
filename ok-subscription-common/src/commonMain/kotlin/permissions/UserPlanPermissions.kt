package permissions

enum class UserPlanPermissions {
    CREATE,

    READ_ADMIN_ONLY,
    READ_PUBLIC,
    READ_CANDIDATE,

    UPDATE_CANDIDATE,

    DELETE_CANDIDATE,
}
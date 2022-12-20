package permissions

enum class UserSubscriptionPermissions {
    CREATE_OWN,

    READ_OWN,
    READ_CANDIDATE,

    UPDATE_OWN,
    UPDATE_CANDIDATE,

    SEARCH,
    SEARCH_OWN,

    DELETE_CANDIDATE,
}
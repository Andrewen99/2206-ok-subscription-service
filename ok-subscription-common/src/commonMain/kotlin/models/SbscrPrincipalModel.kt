package models

import permissions.SbscrUserGroups

data class SbscrPrincipalModel(
    val id: SbscrUserId = SbscrUserId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<SbscrUserGroups> = emptySet()
) {
    companion object {
        val NONE = SbscrPrincipalModel()
    }
}

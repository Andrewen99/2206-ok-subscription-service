package repo

import models.SbscrError

interface IDbResponse<T> {
    val data: T?
    val success: Boolean
    val errors: List<SbscrError>
}
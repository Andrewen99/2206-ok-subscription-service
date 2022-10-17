package validation

import contexts.BaseContext
import helpers.errorValidation
import helpers.fail

fun BaseContext.handleBadFormat(id: String, fieldName: String) {
    val encodedId = id
        .replace("<", "&lt;")
        .replace(">", "%gt")
    fail(
        errorValidation(
            field = fieldName,
            violationCode = "badFormat",
            description = "value $encodedId must contain only"
        )
    )
}
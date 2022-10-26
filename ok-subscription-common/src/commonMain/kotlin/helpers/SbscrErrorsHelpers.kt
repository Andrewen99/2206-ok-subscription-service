package helpers

import contexts.BaseContext
import models.SbscrError
import models.SbscrState

fun Throwable.asSbscrError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = SbscrError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun BaseContext.fail(vararg error: SbscrError) {
    errors.addAll(error)
    state = SbscrState.FAILING
}

fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: SbscrError.Levels = SbscrError.Levels.ERROR
) = SbscrError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level
)

fun errorAdministration(
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    field: String = "",
    violationCode: String,
    description: String,
    exception: Exception? = null,
    level: SbscrError.Levels = SbscrError.Levels.ERROR,
) = SbscrError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    level = level,
    exception = exception,
)
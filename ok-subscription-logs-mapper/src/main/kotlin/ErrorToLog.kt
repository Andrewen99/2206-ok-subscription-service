import models.SbscrError
import ru.otuskotlin.subscription.api.logs.models.ErrorLogModel

fun SbscrError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)
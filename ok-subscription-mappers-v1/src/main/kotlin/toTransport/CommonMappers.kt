package toTransport

import contexts.BaseContext
import ru.otuskotlin.subscription.api.v1.models.ResponseResult
import ru.otuskotlin.subscription.api.v1.models.WsInitResponse
import toTrasportErrors

fun BaseContext.toWsTransportInit() = WsInitResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (errors.isEmpty()) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTrasportErrors()
)
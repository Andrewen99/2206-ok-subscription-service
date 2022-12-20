package ru.otus.otuskotlin.subscription.logging.common

import ru.otus.otuskotlin.subscription.logging.impl.mpLoggerCommon
import kotlin.reflect.KClass

actual fun mpLogger(loggerId: String) = mpLoggerCommon(loggerId)

actual fun mpLogger(cls: KClass<*>) = mpLoggerCommon(cls)

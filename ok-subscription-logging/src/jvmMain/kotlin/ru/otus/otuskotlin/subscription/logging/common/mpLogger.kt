package ru.otus.otuskotlin.subscription.logging.common

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.subscription.logging.jvm.mpLoggerJvm
import kotlin.reflect.KClass

actual fun mpLogger(loggerId: String) = mpLoggerJvm(
    logger = LoggerFactory.getLogger(loggerId) as Logger
)

actual fun mpLogger(cls: KClass<*>) = mpLoggerJvm(
    logger = LoggerFactory.getLogger(cls.java) as Logger
)

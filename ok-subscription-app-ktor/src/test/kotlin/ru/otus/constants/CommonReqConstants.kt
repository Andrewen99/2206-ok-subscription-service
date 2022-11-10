package ru.otus.constants

import ru.otuskotlin.subscription.api.v1.models.Debug
import ru.otuskotlin.subscription.api.v1.models.RequestDebugMode
import ru.otuskotlin.subscription.api.v1.models.RequestDebugStubs

val STUB_DEBUG = Debug(
    mode = RequestDebugMode.STUB,
    stub = RequestDebugStubs.SUCCESS
)

val TEST_DEBUG = Debug(
    mode = RequestDebugMode.TEST
)
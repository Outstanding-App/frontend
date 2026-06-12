package com.tavro.outstanding.base.time

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

actual object Clock {
    @OptIn(ExperimentalTime::class)
    actual fun currentTimeMillis(): Long = Clock.System.now().toEpochMilliseconds()
}

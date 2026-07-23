package com.tavro.outstanding.core.time

actual object Clock {
    actual fun currentTimeMillis(): Long = System.currentTimeMillis()
}

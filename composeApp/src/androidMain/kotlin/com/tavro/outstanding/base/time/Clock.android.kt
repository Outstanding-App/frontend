package com.tavro.outstanding.base.time

actual object Clock {
    actual fun currentTimeMillis(): Long = System.currentTimeMillis()
}

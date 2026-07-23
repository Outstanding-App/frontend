package com.tavro.outstanding.core.time

expect object Clock {
    fun currentTimeMillis(): Long
}

package com.tavro.outstanding.base.time

expect object Clock {
    fun currentTimeMillis(): Long
}

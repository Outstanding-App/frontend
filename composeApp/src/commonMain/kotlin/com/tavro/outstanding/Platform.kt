package com.tavro.outstanding

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

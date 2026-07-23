package com.tavro.outstanding.core.platform

enum class BuildType {
    DEBUG,
    RELEASE;

    companion object {
        fun parse(s: String): BuildType = valueOf(s.uppercase())
    }
}

val BuildType.isDebug: Boolean
    get() = this == BuildType.DEBUG

package com.tavro.outstanding.base

data class Configuration(
    val isPublicBuild: Boolean,
) {
    init {
        if (isPublicBuild) {
            require(!isDebuggable)
        }
    }
}

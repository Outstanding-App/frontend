package com.tavro.outstanding.core

import com.tavro.outstanding.core.platform.isDebuggable

data class Configuration(
    val isPublicBuild: Boolean,
) {
    init {
        if (isPublicBuild) {
            require(!isDebuggable)
        }
    }
}

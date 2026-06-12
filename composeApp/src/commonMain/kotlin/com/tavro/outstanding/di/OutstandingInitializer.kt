package com.tavro.outstanding.di

import com.tavro.outstanding.UiInitializer

class OutstandingInitializer(initializationListeners: List<InitializationListener>) : UiInitializer {
    private val lazyInit by lazy {
        initializationListeners.forEach { it.onInitialize() }
    }

    override fun initialize() {
        lazyInit
    }
}

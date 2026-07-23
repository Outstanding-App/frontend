package com.tavro.outstanding.core

interface UiInitializer {
    fun initialize()

    /**
     * Runs all [initializers] in order the first time [initialize] is called. Subsequent calls are
     * no-ops. Initialization is lazy so that the cost is deferred until the UI is actually needed.
     */
    class Chain(initializers: List<UiInitializer>) {
        private val lazyInit by lazy {
            initializers.forEach { it.initialize() }
        }

        fun initialize() {
            lazyInit
        }
    }
}

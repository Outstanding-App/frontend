package com.tavro.outstanding.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface BaseConfig {
    val debugName: String
}

@Serializable
sealed class Config(override val debugName: String) : BaseConfig {
    @Serializable
    data class Main(val tab: Tab = Tab.Home) : Config("Main") {
        /**
         * Delivered via [Navigator.popWithResult] or [Navigator.popToMain] to tell [MainComponent]
         * which tab to activate after the back stack has been popped back to this destination.
         */
        data class Result(
            val tab: Tab = Tab.Home,
        )

        enum class Tab { Home, Profile }
    }

    @Serializable
    data class Error(val message: String, val stacktrace: String?) : Config("Error")

    @Serializable
    data object AuthToken : Config("AuthToken")

    @Serializable
    data object AuthMissing : Config("AuthMissing")

    @Serializable
    sealed class Onboarding(
        private val internalDebugName: String
    ) : Config("Onboarding$internalDebugName") {
        @Serializable
        data object Login : Onboarding("Login")
    }

}

package com.tavro.outstanding.component.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.pushNew
import com.tavro.outstanding.base.Configuration
import com.tavro.outstanding.navigation.Component
import com.tavro.outstanding.navigation.Config
import com.tavro.outstanding.navigation.Navigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface LoginError {
    val cause: Throwable
    data class Generic(override val cause: Throwable) : LoginError
    data class NoAccount(override val cause: Throwable) : LoginError
}

sealed interface LoginScreenState {
    data object Initial : LoginScreenState
    data class Failure(val cause: Throwable) : LoginScreenState
    data object Loading : LoginScreenState
}

class LoginComponent(
    componentContext: ComponentContext,
    private val navigator: Navigator,
    configuration: Configuration,
) : ComponentContext by componentContext, Component {
    private val _state =
        MutableStateFlow<LoginScreenState>(LoginScreenState.Initial)
    val state = _state.asStateFlow()

    private suspend fun navigateAndReset(config: Config) {
        navigator.pushNew(config)
        delay(500)
        reset()
    }

    private fun onError(cause: Throwable) {
        _state.value = LoginScreenState.Failure(cause = cause)
    }

    private fun reset() {
        _state.value = LoginScreenState.Initial
    }

    fun failedToLogin(error: LoginError) {
        when (error) {
            is LoginError.NoAccount -> {
                reset()
            }
            is LoginError.Generic -> onError(error.cause)
        }
    }

    fun onLogin() {
        // TODO
    }

    fun onLoginStarted() {
        _state.value = LoginScreenState.Loading
    }

    fun onErrorShown() {
        reset()
    }

    @Composable
    override fun Render(modifier: Modifier) = LoginScreen(this, modifier)
}
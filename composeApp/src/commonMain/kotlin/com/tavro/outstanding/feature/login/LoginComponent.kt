package com.tavro.outstanding.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.tavro.outstanding.core.Configuration
import com.tavro.outstanding.data.account.AccountRepository
import com.tavro.outstanding.data.account.AuthService
import com.tavro.outstanding.domain.account.Account
import com.tavro.outstanding.navigation.Component
import com.tavro.outstanding.navigation.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

// TODO(015): Move componentScope related stuff below to it's own file
fun CoroutineScope.withLifecycle(lifecycle: Lifecycle): CoroutineScope {
    lifecycle.doOnDestroy(::cancel)

    return this
}

private var isImmediateSupported: Boolean = true

@Suppress("UnusedReceiverParameter")
private val MainCoroutineDispatcher.immediateOrFallback: MainCoroutineDispatcher
    get() {
        if (isImmediateSupported) {
            try {
                return Dispatchers.Main.immediate
            } catch (ignored: UnsupportedOperationException) {
            } catch (ignored: NotImplementedError) {
            }

            isImmediateSupported = false
        }

        return Dispatchers.Main
    }

const val COMPONENT_CONTEXT_INSTANCE_KEY = "COMPONENT_CONTEXT_COMPONENT_SCOPE"

private fun ComponentContext.componentScope(): CoroutineScope {
    return CoroutineScope(Dispatchers.Main.immediateOrFallback + SupervisorJob()).withLifecycle(lifecycle)
}

val ComponentContext.componentScope: CoroutineScope
    get() = instanceKeeper.getOrCreate(COMPONENT_CONTEXT_INSTANCE_KEY) {
        InstanceKeeper.SimpleInstance(instance = componentScope())
    }.instance

@Serializable
data class UserSession(
    val user_id: String,
    val username: String,
) {
    companion object {
        val UNKNOWN = UserSession(user_id = "", username = "")

        fun createOrUnknown(
            user_id: String?,
            username: String?,
            token: String?,
        ): UserSession =
            if (user_id.isNullOrEmpty()) UNKNOWN
            else UserSession(
                user_id = user_id,
                username = username.orEmpty()
            ).apply {
                this.token = token
            }
    }

    val isUnknown: Boolean
        get() = user_id.isEmpty()

    @Transient
    var token: String? = null
        private set

    fun withToken(token: String) = apply {
        this.token = token
    }
}

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
    private val authService: AuthService,
    private val accountRepository: AccountRepository,
    configuration: Configuration,
) : ComponentContext by componentContext, Component {
    private val _state =
        MutableStateFlow<LoginScreenState>(LoginScreenState.Initial)
    val state = _state.asStateFlow()

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

    fun onLogin(username: String, password: String) {
        _state.value = LoginScreenState.Loading
        componentScope.launch {
            authService.login(username, password)
                .onSuccess { session -> onLoggedIn(session) }
                .onFailure { cause -> _state.value = LoginScreenState.Failure(cause) }
        }
    }

    fun onRegister(username: String, password: String, email: String) {
        println("$username, $password, $email")
        _state.value = LoginScreenState.Loading
        componentScope.launch {
            authService.register(username, password, password, email)
                .onSuccess { session -> onLoggedIn(session) }
                .onFailure { cause -> _state.value = LoginScreenState.Failure(cause) }
        }
    }

    fun onLoggedIn(session: UserSession) {
        require(!session.isUnknown)
        componentScope.launch {
            accountRepository.save(Account(authToken = session.token))
        }
    }

    fun onErrorShown() {
        reset()
    }

    @Composable
    override fun Render(modifier: Modifier) = LoginScreen(this, modifier)
}

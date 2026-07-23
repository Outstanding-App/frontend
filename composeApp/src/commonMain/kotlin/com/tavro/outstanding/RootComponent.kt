package com.tavro.outstanding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DecomposeExperimentFlags
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.GenericComponentContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.router.children.NavigationSource
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.backStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.arkivanov.essenty.lifecycle.doOnPause
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.essenty.statekeeper.SerializableContainer
import com.arkivanov.essenty.statekeeper.consumeRequired
import com.tavro.outstanding.core.Configuration
import com.tavro.outstanding.core.koin.OutstandingKoinComponent
import com.tavro.outstanding.core.time.Clock
import com.tavro.outstanding.core.ui.SystemBarsController
import com.tavro.outstanding.domain.account.AccountProvider
import com.tavro.outstanding.feature.main.MainComponent
import com.tavro.outstanding.feature.main.MainScreen
import com.tavro.outstanding.feature.main.mainScreenTab
import com.tavro.outstanding.navigation.BaseComponent
import com.tavro.outstanding.navigation.Component
import com.tavro.outstanding.navigation.Config
import com.tavro.outstanding.navigation.Navigator
import com.tavro.outstanding.navigation.getComponent
import com.tavro.outstanding.navigation.popToMain
import com.tavro.outstanding.navigation.rememberNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

class RootComponent(
    componentContext: ComponentContext,
    private val accountProvider: AccountProvider,
    val configuration: Configuration,
    private val navigator: Navigator,
    private val mainScope: CoroutineScope
) : ComponentContext by componentContext, OutstandingKoinComponent, BackHandlerOwner {
    private val navigationSessionController = NavigationSessionController()
    private val navigation = navigator

    val stack: Value<ChildStack<Config, Child>> = componentContext.childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Main(),
        handleBackButton = true,
        canRestoreStack = { !navigationSessionController.isExpired() },
        childFactory = ::child,
    )

    // TODO(009): Slot navigation
    private val _finish = MutableStateFlow(false)
    val finish = _finish.asStateFlow()

    init {
        navigator.subscribeToChildStack(stack)

        var previousChildAndConfig: ChildAndConfig? = null
        val lastConfigOnBackStack = MutableStateFlow<Config?>(null)
        stack.subscribe {
            val isForwardNavigation = it.active.configuration != lastConfigOnBackStack
            lastConfigOnBackStack.value = stack.backStack.lastOrNull()?.configuration
            val currentChildAndConfig = ChildAndConfig(child = it.active.instance, config = it.active.configuration)
            previousChildAndConfig = currentChildAndConfig
        }

        // TODO(014): Set developer settings

        doOnPause {
            navigationSessionController.updateExpirationTime()
        }

        doOnResume {
            if (navigationSessionController.isExpired()) {
                if (stack.value.items.firstOrNull()?.configuration is Config.Main) {
                    navigation.popToMain()
                }
            }
        }
        enableDuplicateConfiguration()

        mainScope.launch {
            accountProvider.hasAccountFlow.collect { hasAccount ->
                when (hasAccount) {
                    null -> Unit
                    false -> navigation.replaceAll(Config.Onboarding.Login)
                    true -> navigation.replaceAll(Config.Main())
                }
            }
        }
    }

    @OptIn(ExperimentalDecomposeApi::class)
    private fun enableDuplicateConfiguration() {
        DecomposeExperimentFlags.duplicateConfigurationsEnabled = true
    }

    sealed interface Child : BaseComponent {
        data class ComponentWrapper(val component: Component) : Child {
            override fun accept(value: Any) = component.accept(value)
        }

        class Main(val component: MainComponent) : Child {
            override fun accept(value: Any) {
                when (value) {
                    is Config.Main.Result -> {
                        component.activeTab(value.tab.mainScreenTab)
                    }
                    else -> error("Unexpected result: $value")
                }
            }
        }
    }

    private fun child(config: Config, componentContext: ComponentContext): Child {
        return when (config) {
            is Config.Main -> {
                val mainComponent: MainComponent = get {
                    parametersOf(
                        componentContext,
                        config,
                    )
                }
                Child.Main(component = mainComponent)
            }

            else -> {
                Child.ComponentWrapper(getComponent(config, componentContext))
            }
        }
    }

    fun goBack() {
        navigation.pop { isSuccess ->
            if (!isSuccess) finish()
        }
    }

    private fun finish() {
        _finish.value = true
    }

    /**
     * Expires the navigation backstack after the app has been in the background for longer than
     * [timeout]. On the next resume, the stack is replaced with the initial Main destination so
     * users start fresh rather than returning deep into a stale flow.
     *
     * The expiration timestamp is persisted via [stateKeeper] so it survives process death.
     */
    private inner class NavigationSessionController {
        var timeout: Duration = 1.hours

        private val timoutMillis: Long
            get() = timeout.inWholeMilliseconds

        private var expirationTime: Long? = stateKeeper.consume(
            key = KEY_EXPIRATION_TIME,
            strategy = Long.serializer(),
        )

        init {
            stateKeeper.register(
                key = KEY_EXPIRATION_TIME,
                strategy = Long.serializer(),
                supplier = { expirationTime },
            )
        }

        fun updateExpirationTime() {
            expirationTime = Clock.currentTimeMillis() + timoutMillis
        }

        fun isExpired(): Boolean {
            val time = consumeExpirationTime()
            return time != null && Clock.currentTimeMillis() > time
        }

        private fun consumeExpirationTime(): Long? {
            val result = expirationTime
            expirationTime = null
            return result
        }
    }

    data class ChildAndConfig(val config: Config, val child: Child)

    companion object {
        private const val KEY_EXPIRATION_TIME = "NavSessionController_ExpirationTime"
    }
}

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    SystemBarsController()
    val navigator = rememberNavigator()
    Children(
        stack = component.stack,
        modifier = modifier
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.Main -> MainScreen(component = child.component)

            is RootComponent.Child.ComponentWrapper -> {
                child.component.Render(Modifier)
            }
        }
    }
}

/**
 * Wraps Decompose's [childStack] with a [canRestoreStack] gate. When the gate returns `false`
 * the persisted stack is discarded and [initialConfiguration] is used instead, preventing the
 * user from returning to a stale flow.
 */
fun <Ctx : GenericComponentContext<Ctx>, C : Any, T : Any> Ctx.childStack(
    source: NavigationSource<StackNavigation.Event<C>>,
    serializer: KSerializer<C>?,
    initialConfiguration: C,
    key: String = "DefaultChildStack",
    handleBackButton: Boolean = false,
    canRestoreStack: () -> Boolean = { true },
    childFactory: (configuration: C, Ctx) -> T
): Value<ChildStack<C, T>> = childStack(
    source = source,
    initialStack = { listOf(initialConfiguration) },
    saveStack = { stack ->
        serializer?.run {
            SerializableContainer(
                value = stack,
                strategy = ListSerializer(this),
            )
        }
    },
    restoreStack = { container ->
        if (canRestoreStack()) {
            serializer?.run { container.consumeRequired(strategy = ListSerializer(this)) }
        } else {
            null
        }
    },
    key = key,
    handleBackButton = handleBackButton,
    childFactory = childFactory,
)

// TODO(009): Ctx.childSlotOverride

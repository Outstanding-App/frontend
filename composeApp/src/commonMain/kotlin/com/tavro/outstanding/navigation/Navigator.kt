package com.tavro.outstanding.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popWhile
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.tavro.outstanding.core.Configuration
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.getKoin

@Composable
fun rememberNavigator(): Navigator {
    val koin = getKoin()
    return remember { koin.get<Navigator>() }
}

class Navigator internal constructor(
    private val stackNavigation: StackNavigation<Config>,
    // TODO(009): slotNavigator: SlotNavigation<SlotConfig>,
    mainScope: CoroutineScope,
    private val configuration: Configuration,
) : StackNavigation<Config> by stackNavigation {
    private var childStack: Value<ChildStack<Config, BaseComponent>>? = null

    fun subscribeToChildStack(childStack: Value<ChildStack<Config, BaseComponent>>) {
        this.childStack = childStack
    }

    /** Delivers [value] to the currently active component via [BaseComponent.accept]. */
    fun deliver(value: Any) {
        childStack?.active?.instance?.accept(value)
    }

    /** Pops the back stack and delivers [result] to the new active component if the pop succeeded. */
    fun popWithResult(result: Any) {
        pop { if (it) deliver(result) }
    }

    /** Pops until [predicate] is false, then delivers [result] to the newly active component. */
    fun popWhileWithResult(predicate: (Config) -> Boolean, result: Any) {
        popWhile(predicate = predicate, onComplete = { deliver(result) })
    }
}

@Suppress("SpreadOperator")
fun Navigator.navigateToStack(stack: List<Config>) {
    replaceAll(*stack.toTypedArray())
}

fun Navigator.popToMain(tab: Config.Main.Tab = Config.Main.Tab.Home) {
    popWhileWithResult(predicate = { it !is Config.Main }, result = Config.Main.Result(tab))
}

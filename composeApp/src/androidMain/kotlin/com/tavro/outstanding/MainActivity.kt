package com.tavro.outstanding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.retainedComponent
import com.tavro.outstanding.core.UiInitializer
import com.tavro.outstanding.core.koin.OutstandingKoinComponent
import com.tavro.outstanding.core.ui.SimpleEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity(), OutstandingKoinComponent {
    private val uiInitializer: UiInitializer.Chain by inject()
    private val mainScope: CoroutineScope by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val root = retainedComponent { context ->
            get<RootComponent> {
                parametersOf(context)
            }
        }
        val initialized = MutableStateFlow(false)
        setContent {
            if (!initialized.collectAsState().value) {
                return@setContent
            }
            val finish by root.finish.collectAsState()
            if (finish) {
                SimpleEffect(Unit) {
                    finish()
                }
            }
            App(
                component = root,
                modifier = Modifier.fillMaxSize()
            )
        }

        // yield() lets setContent run first so the window is attached before initializers execute.
        // initialized gates rendering so the first frame is never shown with a blank/loading state.
        mainScope.launch {
            yield()
            uiInitializer.initialize()
            initialized.value = true
        }
    }
}

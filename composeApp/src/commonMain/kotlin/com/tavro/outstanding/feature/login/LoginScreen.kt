package com.tavro.outstanding.feature.login

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import com.tavro.outstanding.core.ui.UserMessage
import com.tavro.outstanding.core.ui.bindErrorToSnackbarHostState
import com.tavro.outstanding.core.ui.navigateToErrorDetails
import com.tavro.outstanding.core.ui.scaffoldContentWindowInsets
import com.tavro.outstanding.designsystem.components.JadeFilledButton
import com.tavro.outstanding.designsystem.components.JadeScaffold
import com.tavro.outstanding.designsystem.components.JadeTextField
import com.tavro.outstanding.designsystem.components.TextFieldDefaults
import com.tavro.outstanding.designsystem.components.TextFieldSize
import com.tavro.outstanding.designsystem.theme.JadeTheme
import com.tavro.outstanding.designsystem.tokens.Space
import com.tavro.outstanding.navigation.rememberNavigator

@Composable
fun LoginScreen(
    component: LoginComponent,
    modifier: Modifier = Modifier
) {
    val state = component.state.collectAsState().value
    val error = if (state is LoginScreenState.Failure) UserMessage.Error(
        text = "Failed to sign in",
        cause = state.cause
    ) else null
    val navigator = rememberNavigator()
    val snackbarHostState = bindErrorToSnackbarHostState(
        error = error,
        onErrorShown = component::onErrorShown,
        onErrorDetailsClick = navigator::navigateToErrorDetails
    )

    JadeScaffold(
        modifier = modifier,
        containerColor = JadeTheme.colorScheme.background,
        contentWindowInsets = scaffoldContentWindowInsets(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        content = { contentPadding ->
            LoginScreenContent(
                loginState = state,
                onLogin = component::onLogin,
                onLoginFailed = component::failedToLogin,
                onLoginClicked = component::onLoginStarted,
                contentPadding = contentPadding,
                modifier = Modifier.fillMaxSize()
            )
        }
    )
}

@Composable
internal fun LoginScreenContent(
    loginState: LoginScreenState,
    onLogin: () -> Unit,
    onLoginFailed: (error: LoginError) -> Unit,
    onLoginClicked: () -> Unit,
    usernameFocusRequester: FocusRequester? = null,
    usernameInteractionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    usernameKeyboardActions: KeyboardActions = TextFieldDefaults.defaultKeyboardActions,
    usernameKeyboardOptions: KeyboardOptions = TextFieldDefaults.defaultKeyboardOptions,
    passwordFocusRequester: FocusRequester? = null,
    passwordInteractionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    passwordKeyboardActions: KeyboardActions = TextFieldDefaults.defaultKeyboardActions,
    passwordKeyboardOptions: KeyboardOptions = TextFieldDefaults.defaultKeyboardOptions,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues.Zero,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Space.md),
        modifier = modifier
            .statusBarsPadding()
            .padding(contentPadding)
            .padding(horizontal = Space.lg)
    ) {
        JadeTextField(
            size = TextFieldSize.Normal,
            text = TextFieldValue(text = ""),
            onTextChange = {},
            supportingText = null,
            placeholder = "username",
            keyboardActions = usernameKeyboardActions,
            keyboardOptions = usernameKeyboardOptions,
            singleLine = true,
            maxLines = 1,
            minLines = TextFieldDefaults.DEFAULT_MIN_LINES,
            visualTransformation = TextFieldDefaults.defaultVisualTransformation,
            interactionSource = usernameInteractionSource,
            readOnly = false,
            focusRequester = usernameFocusRequester,
            autoSizeText = TextFieldDefaults.DEFAULT_AUTO_SIZE_TEXT,
            modifier = Modifier.fillMaxWidth(),
        )
        JadeTextField(
            size = TextFieldSize.Normal,
            text = TextFieldValue(text = ""),
            onTextChange = {},
            supportingText = null,
            placeholder = "password",
            keyboardActions = passwordKeyboardActions,
            keyboardOptions = passwordKeyboardOptions,
            singleLine = true,
            maxLines = 1,
            minLines = TextFieldDefaults.DEFAULT_MIN_LINES,
            visualTransformation = TextFieldDefaults.defaultVisualTransformation,
            interactionSource = passwordInteractionSource,
            readOnly = false,
            focusRequester = passwordFocusRequester,
            autoSizeText = TextFieldDefaults.DEFAULT_AUTO_SIZE_TEXT,
            modifier = Modifier.fillMaxWidth(),
        )
        JadeFilledButton(
            text = "Login",
            onClick = onLoginClicked,
            enabled = loginState is LoginScreenState.Initial,
            minFontSize = TextUnit.Unspecified,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

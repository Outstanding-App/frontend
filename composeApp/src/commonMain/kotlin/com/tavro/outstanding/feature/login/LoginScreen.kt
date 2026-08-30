package com.tavro.outstanding.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import com.tavro.outstanding.core.ui.UserMessage
import com.tavro.outstanding.core.ui.bindErrorToSnackbarHostState
import com.tavro.outstanding.core.ui.navigateToErrorDetails
import com.tavro.outstanding.core.ui.scaffoldContentWindowInsets
import com.tavro.outstanding.designsystem.components.JadeFilledButton
import com.tavro.outstanding.designsystem.components.JadeOutlinedButton
import com.tavro.outstanding.designsystem.components.JadeScaffold
import com.tavro.outstanding.designsystem.components.JadeText
import com.tavro.outstanding.designsystem.components.JadeTextField
import com.tavro.outstanding.designsystem.components.TextFieldDefaults
import com.tavro.outstanding.designsystem.components.TextFieldSize
import com.tavro.outstanding.designsystem.components.visualTransformationIfSupported
import com.tavro.outstanding.designsystem.theme.JadeTheme
import com.tavro.outstanding.designsystem.tokens.Space
import com.tavro.outstanding.navigation.rememberNavigator
import com.tavro.outstanding.resources.Strings
import org.jetbrains.compose.resources.vectorResource
import outstanding.composeapp.generated.resources.Res
import outstanding.composeapp.generated.resources.logo

@Composable
fun LoginScreen(
    component: LoginComponent,
    modifier: Modifier = Modifier
) {
    val state = component.state.collectAsState().value
    val error = if (state is LoginScreenState.Failure) UserMessage.Error(
        text = Strings.error_message_failed_login,
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
                onLoginClicked = component::onLogin,
                onRegisterClicked = component::onRegister,
                contentPadding = contentPadding,
                modifier = Modifier.fillMaxSize()
            )
        }
    )
}

@Composable
internal fun LoginScreenContent(
    loginState: LoginScreenState,
    onLoginClicked: (username: String, password: String) -> Unit,
    onRegisterClicked: (username: String, password: String, email: String) -> Unit,
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
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    var registerUsername by remember { mutableStateOf(TextFieldValue("")) }
    var registerPassword by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }

    var showLogin by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(Space.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .statusBarsPadding()
            .padding(contentPadding)
            .padding(Space.lg)
    ) {
        val titleStyle = JadeTheme.typography.displayLarge
        val logoHeight = with(LocalDensity.current) { titleStyle.lineHeight.toDp() }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                imageVector = vectorResource(Res.drawable.logo),
                contentDescription = Strings.content_desc_outstanding_logo,
                modifier = Modifier
                    .height(logoHeight)
                    .aspectRatio(200f / 240f)
            )
            JadeText(
                text = Strings.app_name,
                style = titleStyle
            )
        }
        if (showLogin) {
            JadeTextField(
                size = TextFieldSize.Normal,
                text = username,
                onTextChange = { username = it },
                supportingText = null,
                placeholder = Strings.placeholder_username,
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
                text = password,
                onTextChange = { password = it },
                supportingText = null,
                placeholder = Strings.placeholder_password,
                keyboardActions = passwordKeyboardActions,
                keyboardOptions = passwordKeyboardOptions,
                singleLine = true,
                maxLines = 1,
                minLines = TextFieldDefaults.DEFAULT_MIN_LINES,
                visualTransformation = visualTransformationIfSupported(PasswordVisualTransformation()),
                interactionSource = passwordInteractionSource,
                readOnly = false,
                focusRequester = passwordFocusRequester,
                autoSizeText = TextFieldDefaults.DEFAULT_AUTO_SIZE_TEXT,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.weight(1.0f))
            JadeFilledButton(
                text = Strings.button_label_login,
                onClick = { onLoginClicked(username.text, password.text) },
                enabled = loginState is LoginScreenState.Initial,
                minFontSize = TextUnit.Unspecified,
                modifier = Modifier.fillMaxWidth()
            )
            JadeOutlinedButton(
                text = Strings.button_label_no_account,
                onClick = { showLogin = false },
                enabled = loginState is LoginScreenState.Initial,
                minFontSize = TextUnit.Unspecified,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            JadeTextField(
                size = TextFieldSize.Normal,
                text = registerUsername,
                onTextChange = { registerUsername = it },
                supportingText = null,
                placeholder = Strings.placeholder_username,
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
                text = registerPassword,
                onTextChange = { registerPassword = it },
                supportingText = null,
                placeholder = Strings.placeholder_password,
                keyboardActions = passwordKeyboardActions,
                keyboardOptions = passwordKeyboardOptions,
                singleLine = true,
                maxLines = 1,
                minLines = TextFieldDefaults.DEFAULT_MIN_LINES,
                visualTransformation = visualTransformationIfSupported(PasswordVisualTransformation()),
                interactionSource = passwordInteractionSource,
                readOnly = false,
                focusRequester = passwordFocusRequester,
                autoSizeText = TextFieldDefaults.DEFAULT_AUTO_SIZE_TEXT,
                modifier = Modifier.fillMaxWidth(),
            )
            JadeTextField(
                size = TextFieldSize.Normal,
                text = email,
                onTextChange = { email = it },
                supportingText = null,
                placeholder = Strings.placeholder_email,
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
            Spacer(modifier = Modifier.weight(1.0f)) 
            JadeFilledButton(
                text = Strings.button_label_register,
                onClick = { onRegisterClicked(registerUsername.text, registerPassword.text, email.text) },
                enabled = loginState is LoginScreenState.Initial,
                minFontSize = TextUnit.Unspecified,
                modifier = Modifier.fillMaxWidth()
            )
            JadeOutlinedButton(
                text = Strings.button_label_already_registered,
                onClick = { showLogin = true },
                enabled = loginState is LoginScreenState.Initial,
                minFontSize = TextUnit.Unspecified,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

package com.tavro.outstanding.resources

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import outstanding.composeapp.generated.resources.Res
import outstanding.composeapp.generated.resources.action_label_details
import outstanding.composeapp.generated.resources.app_name
import outstanding.composeapp.generated.resources.button_label_already_registered
import outstanding.composeapp.generated.resources.button_label_log_out
import outstanding.composeapp.generated.resources.button_label_login
import outstanding.composeapp.generated.resources.button_label_no_account
import outstanding.composeapp.generated.resources.button_label_register
import outstanding.composeapp.generated.resources.content_desc_outstanding_logo
import outstanding.composeapp.generated.resources.error_message_failed_login
import outstanding.composeapp.generated.resources.placeholder_email
import outstanding.composeapp.generated.resources.placeholder_password
import outstanding.composeapp.generated.resources.placeholder_username
import outstanding.composeapp.generated.resources.title_map
import outstanding.composeapp.generated.resources.title_profile

internal object Strings {
    val action_label_details: String
        @Composable get() = stringResource(Res.string.action_label_details)

    val app_name: String
        @Composable get() = stringResource(Res.string.app_name)

    val button_label_already_registered: String
        @Composable get() = stringResource(Res.string.button_label_already_registered)

    val button_label_log_out: String
        @Composable get() = stringResource(Res.string.button_label_log_out)

    val button_label_login: String
        @Composable get() = stringResource(Res.string.button_label_login)

    val button_label_no_account: String
        @Composable get() = stringResource(Res.string.button_label_no_account)

    val button_label_register: String
        @Composable get() = stringResource(Res.string.button_label_register)

    val content_desc_outstanding_logo: String
        @Composable get() = stringResource(Res.string.content_desc_outstanding_logo)

    val error_message_failed_login: String
        @Composable get() = stringResource(Res.string.error_message_failed_login)

    val placeholder_email: String
        @Composable get() = stringResource(Res.string.placeholder_email)

    val placeholder_password: String
        @Composable get() = stringResource(Res.string.placeholder_password)

    val placeholder_username: String
        @Composable get() = stringResource(Res.string.placeholder_username)

    val title_map: String
        @Composable get() = stringResource(Res.string.title_map)

    val title_profile: String
        @Composable get() = stringResource(Res.string.title_profile)
}

package com.example.auth.presetation.login

import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.presetation.register.RegisterAction
import com.example.core.presentation.designsystem.AppFont
import com.example.core.presentation.designsystem.EmailIcon
import com.example.core.presentation.designsystem.GumrunGray
import com.example.core.presentation.designsystem.GumrunTheme
import com.example.core.presentation.designsystem.R
import com.example.core.presentation.designsystem.components.GradientBackground
import com.example.core.presentation.designsystem.components.GumrunActionButton
import com.example.core.presentation.designsystem.components.GumrunPasswordTextField
import com.example.core.presentation.designsystem.components.GumrunTextField
import com.example.presentation.ui.observeAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable

fun LoginScreenRoot(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    observeAsEvents(viewModel.events) { event ->
        when (event) {
            is LoginEvent.Error -> {
                keyboardController?.hide()
                Toast.makeText(context, event.error.asString(context), Toast.LENGTH_LONG).show()
            }

            LoginEvent.LoginSuccess -> {
                Toast.makeText(context, R.string.youre_logged_in, Toast.LENGTH_LONG).show()
                onLoginSuccess()
            }
        }
    }
    LoginScreen(
        state = viewModel.state,
        onAction = {action ->
            when(action){
                LoginAction.OnLoginClick -> onSignUpClick()
                LoginAction.OnRegisterClick -> TODO()
                LoginAction.OnTogglePasswordVisibilityClick -> TODO()
            }
            viewModel.onAction(action)

        }
    )
}

@Composable

private fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {
    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(vertical = 32.dp)
                .padding(top = 16.dp)

        ) {
            Text(
                text = stringResource(R.string.hi_there),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(R.string.gumrun_welcome_text),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(48.dp))
            GumrunTextField(
                state = state.email,
                startIcon = EmailIcon,
                endIcon = null,
                hint = stringResource(R.string.email_hint),
                title = stringResource(R.string.email),
                modifier = Modifier.fillMaxSize(),
            )
            Spacer(modifier = Modifier.height(16.dp))
            GumrunPasswordTextField(
                state = state.password,
                hint = stringResource(R.string.password),
                title = stringResource(R.string.password),
                modifier = Modifier.fillMaxWidth(),
                isPasswordVisible = state.isPasswordVisible,
                onPasswordVisibilityChange = { onAction(LoginAction.OnTogglePasswordVisibilityClick) },
            )
            Spacer(modifier = Modifier.height(32.dp))
            GumrunActionButton(
                text = stringResource(R.string.log_in),
                enabled = state.canLogin,
                onClick = {
                    onAction(LoginAction.OnLoginClick)
                },
                isLoading = state.isLoggingIn,
                modifier = Modifier.fillMaxWidth(),
            )

            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = AppFont,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                ) {
                    append(stringResource(R.string.dont_have_an_account) + " ")
                    pushStringAnnotation(
                        tag = "clickable_text", annotation = stringResource(R.string.sign_up)
                    )
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = AppFont,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    ) {
                        append(stringResource(R.string.sign_up))
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                ClickableText(
                    text = annotatedString, onClick = { offset ->
                        annotatedString.getStringAnnotations(
                            tag = "clickable_text", start = offset, end = offset
                        ).firstOrNull()?.let { onAction(LoginAction.OnRegisterClick) }
                    })
            }


        }
    }

}

@Preview
@Composable
private fun LoginScreenRotScreenPreview() {

    GumrunTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}

        )

    }

}
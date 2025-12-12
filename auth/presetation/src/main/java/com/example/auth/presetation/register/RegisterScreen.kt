package com.example.auth.presetation.register

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.domain.PasswordValidationState
import com.example.auth.domain.UserDataValidator
import com.example.core.presentation.designsystem.AppFont
import com.example.core.presentation.designsystem.CheckIcon
import com.example.core.presentation.designsystem.CrossIcon
import com.example.core.presentation.designsystem.EmailIcon
import com.example.core.presentation.designsystem.GumrunDarkRed
import com.example.core.presentation.designsystem.components.GradientBackground
import com.example.core.presentation.designsystem.GumrunGray
import com.example.core.presentation.designsystem.GumrunGreen
import com.example.core.presentation.designsystem.GumrunTheme
import com.example.core.presentation.designsystem.R
import com.example.core.presentation.designsystem.components.GumrunActionButton
import com.example.core.presentation.designsystem.components.GumrunPasswordTextField
import com.example.core.presentation.designsystem.components.GumrunTextField
import com.example.presentation.ui.observeAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreenRoot(
    onSignInClick: () -> Unit,
    onSuccessfulRegistration: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    observeAsEvents(
        flow = viewModel.event
    ) { event ->
        when (event) {
            is RegisterEvent.RegisterError -> {
                keyboardController?.hide()
                Toast.makeText(context, event.error.asString(context), Toast.LENGTH_LONG).show()
            }

            RegisterEvent.RegistrationSuccess -> {
                Toast.makeText(context, R.string.registration_success, Toast.LENGTH_LONG).show()
                onSuccessfulRegistration()
            }
        }

    }

    RegisterScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun RegisterScreen(
    state: RegisterState, onAction: (RegisterAction) -> Unit
) {
    GradientBackground {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(vertical = 32.dp)
                .padding(top = 16.dp)

        ) {
            Text(
                text = stringResource(R.string.create_content),
                style = MaterialTheme.typography.headlineMedium
            )
            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = AppFont,
                        color = GumrunGray,
                    )
                ) {
                    append(stringResource(R.string.already_have_an_account) + " ")
                    pushStringAnnotation(
                        tag = "clickable_text", annotation = stringResource(R.string.log_in)
                    )
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = AppFont,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    ) {
                        append(stringResource(R.string.log_in))
                    }
                }
            }

            ClickableText(
                text = annotatedString, onClick = { offset ->
                    annotatedString.getStringAnnotations(
                        tag = "clickable_text", start = offset, end = offset
                    ).firstOrNull()?.let { onAction(RegisterAction.OnLoginClick) }
                })

            Spacer(modifier = Modifier.height(48.dp))

            GumrunTextField(
                state = state.email,
                startIcon = EmailIcon,
                endIcon = if (state.isEmailValid) CheckIcon else null,
                hint = stringResource(R.string.email_hint),
                title = stringResource(R.string.email),
                modifier = Modifier.fillMaxWidth(),
                additionalInfo = stringResource(R.string.must_be_valid_email),
                keyboardType = KeyboardType.Email,
            )

            Spacer(modifier = Modifier.height(16.dp))

            GumrunPasswordTextField(
                state = state.password,
                isPasswordVisible = state.isPasswordVisible,
                onPasswordVisibilityChange = { onAction(RegisterAction.OnTogglePasswordVisibilityClick) },
                hint = stringResource(R.string.password),
                title = stringResource(R.string.password),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(32.dp))

            PasswordRequirement(
                text = stringResource(
                    R.string.at_least_x_characters, UserDataValidator.MIN_PASSWORD_LENGTH
                ),
                isValid = state.passwordValidationState.hasMinimumLength,
            )
            Spacer(modifier = Modifier.height(4.dp))

            PasswordRequirement(
                text = stringResource(
                    R.string.at_least_one_number,
                ),
                isValid = state.passwordValidationState.hasNumber,
            )
            Spacer(modifier = Modifier.height(4.dp))

            PasswordRequirement(
                text = stringResource(
                    R.string.contain_lowercase_char,
                ),
                isValid = state.passwordValidationState.hasLowercaseChar,
            )
            Spacer(modifier = Modifier.height(4.dp))

            PasswordRequirement(
                text = stringResource(
                    R.string.contain_uppercase_char, UserDataValidator.MIN_PASSWORD_LENGTH
                ),
                isValid = state.passwordValidationState.hasUppercaseChar,
            )
            Spacer(modifier = Modifier.height(16.dp))

            GumrunActionButton(
                text = stringResource(R.string.register),
                isLoading = state.isRegistering,
                enabled = state.canRegister,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onAction(RegisterAction.OnRegisterClick) })
        }
    }
}

@Composable
fun PasswordRequirement(
    text: String, isValid: Boolean, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = if (isValid) CheckIcon else CrossIcon,
            contentDescription = null,
            tint = if (isValid) GumrunGreen else GumrunDarkRed

        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
    }
}

@Preview
@Composable
private fun RegisterScreenScreenPreview() {

    GumrunTheme {

        RegisterScreen(
            state = RegisterState(
                passwordValidationState = PasswordValidationState(
                    hasMinimumLength = true,
                    hasNumber = true,
                    hasLowercaseChar = true,
                    hasUppercaseChar = true
                )
            ),
            onAction = {}
        )

    }

}
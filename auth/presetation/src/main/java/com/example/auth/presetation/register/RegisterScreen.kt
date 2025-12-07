package com.example.auth.presetation.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.presentation.designsystem.AppFont
import com.example.core.presentation.designsystem.components.GradientBackground
import com.example.core.presentation.designsystem.GumrunGray
import com.example.core.presentation.designsystem.GumrunTheme
import com.example.core.presentation.designsystem.R
import org.koin.androidx.compose.koinViewModel

@Composable

fun RegisterScreenScreenRoot(

    viewModel: RegisterViewModel = koinViewModel()

) {

    RegisterScreen(

        state = viewModel.state,

        onAction = viewModel::onAction

    )

}

@Composable

private fun RegisterScreen(

    state: RegisterState,

    onAction: (RegisterAction) -> Unit

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
                        tag = "clickable_text",
                        annotation = stringResource(R.string.log_in)
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
                text = annotatedString,
                onClick = { offset ->
                    annotatedString.getStringAnnotations(
                        tag = "clickable_text",
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let { onAction(RegisterAction.OnLoginClick) }
                }
            )
        }
    }

}

@Preview

@Composable

private fun RegisterScreenScreenPreview() {

    GumrunTheme {

        RegisterScreen(

            state = RegisterState(),

            onAction = {}

        )

    }

}
package com.example.auth.presetation.intro

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.example.core.presentation.designsystem.GumrunTheme

// Create a separate composable component that can take viewModel, navController etc
// Will render child components
@Composable
fun IntroScreenRoot(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit
) {
    IntroScreen(
        onAction = { action ->
            when (action) {
                IntroAction.OnSignInClick -> onSignInClick()
                IntroAction.OnSignUpClick -> onSignUpClick()
            }
        }
    )
}

// Child should receive state and events
@Composable
fun IntroScreen(
    onAction: (IntroAction) -> Unit
) {


}

@Preview
@Composable
private fun IntroScreenPreview() {
    GumrunTheme {
        IntroScreen { }
    }
}
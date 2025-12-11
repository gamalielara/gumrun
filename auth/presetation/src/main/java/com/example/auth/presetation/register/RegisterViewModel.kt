package com.example.auth.presetation.register

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import com.example.auth.domain.UserDataValidator
import com.example.auth.presetation.utils.textAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class RegisterViewModel(
    private val userDataValidator: UserDataValidator
) : ViewModel() {
    var state by mutableStateOf(RegisterState())
        private set


    init {
        state.email.textAsFlow().onEach { email ->
            val _isEmailValid = userDataValidator.isValidEmail(email.toString())
            state = state.copy(
                isEmailValid = _isEmailValid,
                canRegister = _isEmailValid && state.passwordValidationState.isValidPassword && !state.isRegistering
            )

        }.launchIn(viewModelScope)

        state.password.textAsFlow().onEach { password ->
            val _passwordValidationState = userDataValidator.validatePassword(password.toString())
            state = state.copy(
                passwordValidationState = _passwordValidationState,
                canRegister = state.isEmailValid && _passwordValidationState.isValidPassword

            )

        }.launchIn(viewModelScope)
    }

    fun onAction(action: RegisterAction) {

    }
}
package com.example.auth.presetation.register

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.auth.domain.AuthRepository
import com.example.auth.domain.UserDataValidator
import com.example.auth.presetation.utils.textAsFlow
import com.example.core.domain.util.DataError
import com.example.core.domain.util.Result
import com.example.core.presentation.designsystem.R
import com.example.presentation.ui.UIText
import com.example.presentation.ui.asUIText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class RegisterViewModel(
    private val userDataValidator: UserDataValidator,
    private val authRepository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(RegisterState())
        private set

    private val eventChannel = Channel<RegisterEvent>()
    val event = eventChannel.receiveAsFlow()


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
        when (action) {
            RegisterAction.OnLoginClick -> TODO()

            RegisterAction.OnRegisterClick -> {
                register()
            }

            RegisterAction.OnTogglePasswordVisibilityClick -> {
                state = state.copy(isPasswordVisible = !state.isPasswordVisible)
            }

        }

    }

    private fun register() {
        viewModelScope.launch {
            state = state.copy(isRegistering = true)
            val result = authRepository.register(
                email = state.email.text.toString().trim(),
                password = state.password.text.toString()
            )
            state = state.copy(isRegistering = false)

            when (result) {
                is Result.Error -> {
                    if (result.error == DataError.Network.CONFLICT) {
                        eventChannel.send(
                            RegisterEvent.RegisterError(
                                UIText.StringResource(R.string.error_email_existed)
                            )
                        )

                    } else {

                        eventChannel.send(RegisterEvent.RegisterError(result.error.asUIText()))
                    }
                }

                is Result.Success -> {
                    eventChannel.send(RegisterEvent.RegistrationSuccess)

                }
            }
        }
    }
}
package com.example.auth.presetation.register

import com.example.presentation.ui.UIText

sealed interface RegisterEvent {
    data object RegistrationSuccess : RegisterEvent
    data class RegisterError(val error: UIText) : RegisterEvent
}
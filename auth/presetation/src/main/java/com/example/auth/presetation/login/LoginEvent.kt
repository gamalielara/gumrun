package com.example.auth.presetation.login

import com.example.presentation.ui.UIText

sealed interface LoginEvent {
    data class Error(val error: UIText) : LoginEvent
    data object LoginSuccess : LoginEvent
}
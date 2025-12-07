package com.example.auth.presetation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue


class RegisterViewModel : ViewModel() {
    var state by mutableStateOf(RegisterState())
        private set


    fun onAction(action: RegisterAction) {

    }
}